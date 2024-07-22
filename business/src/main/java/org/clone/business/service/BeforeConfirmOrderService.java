package org.clone.business.service;

import cn.hutool.core.date.DateTime;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.clone.business.domain.ConfirmOrder;
import org.clone.business.dto.ConfirmOrderMQDto;
import org.clone.business.enums.ConfirmOrderStatusEnum;
import org.clone.business.enums.RocketMQTopicEnum;
import org.clone.business.mapper.ConfirmOrderMapper;
import org.clone.business.req.ConfirmOrderDoneReq;
import org.clone.business.req.ConfirmOrderTicketReq;
import org.clone.common.context.LoginMemberContext;
import org.clone.common.exception.BusinessException;
import org.clone.common.util.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.clone.common.exception.BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION;
import static org.clone.common.exception.BusinessExceptionEnum.CONFIRM_ORDER_SK_TOKEN_FAIL;

@Service
public class BeforeConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private SkTokenService skTokenService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    public RocketMQTemplate rocketMQTemplate;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @SentinelResource(value = "done", blockHandler = "doneBlock")
    public Long beforeConfirmOrderService(ConfirmOrderDoneReq req) {
        req.setMemberId(LoginMemberContext.getId());
        //校验令牌余量
        boolean validSkToken = skTokenService.validSkToken(req.getDate(), req.getTrainCode(), LoginMemberContext.getId());
        if (validSkToken) {
            LOG.info("Tokens are abundant");
        } else {
            LOG.info("No enough token");
            throw new BusinessException(CONFIRM_ORDER_SK_TOKEN_FAIL);
        }
        //redis 分布式锁解决超卖
        //redis key 要区分不同车次
        //String lockKey = CONFIRM_ORDER + DateUtil.formatDate(req.getDate()) + "-" + req.getTrainCode();
        //即redis的setnx
//        Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
//        if (setIfAbsent) {
//            LOG.info("Got lock");
//        } else {
//            LOG.info("Fail to get lock");
//            throw new BusinessException(CONFIRM_ORDER_LOCK_FAIL);
//        }

        DateTime now = DateTime.now();
        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String departure = req.getDeparture();
        String destination = req.getDestination();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();

        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setDate(date);
        confirmOrder.setMemberId(req.getMemberId());
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setDeparture(departure);
        confirmOrder.setDestination(destination);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        confirmOrderMapper.insert(confirmOrder);

//        RLock lock = null;
//        try {
//            lock = redissonClient.getLock(lockKey); //构造锁
//            boolean tryLock = lock.tryLock(0, TimeUnit.SECONDS); //带看门狗
//            // boolean tryLock = lock.tryLock(0, 10, TimeUnit.SECONDS);//不带看门狗
//            if (tryLock) {
//                LOG.info("Got lock");
//
//            } else {
//                LOG.info("Fail to get lock");
//                throw new BusinessException(CONFIRM_ORDER_LOCK_FAIL);
//            }
//        } catch (InterruptedException e) {
//            LOG.error("Fail to purchase", e);
//        } finally {
//            //stringRedisTemplate.delete(lockKey);
//            LOG.info("Purchase finishes, release the lock");
//            if (null != lock && lock.isHeldByCurrentThread()) lock.unlock(); //lock空或不是当前线程锁
//        }
        //req.setLogId(MDC.get("LOG_ID"));
        //String reqJson = JSON.toJSONString(req);
        ConfirmOrderMQDto confirmOrderMQDto = new ConfirmOrderMQDto();
        confirmOrderMQDto.setDate(req.getDate());
        confirmOrderMQDto.setLogId(MDC.get("LOG_ID"));
        confirmOrderMQDto.setTrainCode(req.getTrainCode());
        String reqJson = JSON.toJSONString(confirmOrderMQDto);
        LOG.info("Start MQ: {}", reqJson);
        rocketMQTemplate.convertAndSend(RocketMQTopicEnum.CONFIRM_ORDER.getCode(), reqJson);
        LOG.info("Sending finished, start to order");

        return confirmOrder.getId(); //返回订单id，便于排队排序
    }
    public void doneBlock(ConfirmOrderDoneReq req, BlockException e) {
        LOG.info("Order is restricted: {}", req);
        throw new BusinessException(CONFIRM_ORDER_FLOW_EXCEPTION);
    }
}
