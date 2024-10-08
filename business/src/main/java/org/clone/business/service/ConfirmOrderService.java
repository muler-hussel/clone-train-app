package org.clone.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.clone.business.domain.*;
import org.clone.business.dto.ConfirmOrderMQDto;
import org.clone.business.enums.ConfirmOrderStatusEnum;
import org.clone.business.enums.SeatColEnum;
import org.clone.business.enums.SeatTypeEnum;
import org.clone.business.mapper.ConfirmOrderMapper;
import org.clone.business.req.ConfirmOrderDoneReq;
import org.clone.business.req.ConfirmOrderQueryReq;
import org.clone.business.req.ConfirmOrderSaveReq;
import org.clone.business.req.ConfirmOrderTicketReq;
import org.clone.business.resp.ConfirmOrderQueryResp;
import org.clone.common.exception.BusinessException;
import org.clone.common.response.PageRes;
import org.clone.common.util.SnowUtil;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.clone.business.enums.RedisKeyPreEnum.CONFIRM_ORDER;
import static org.clone.common.exception.BusinessExceptionEnum.*;

@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private AfterConfirmOrder afterConfirmOrder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    SkTokenService skTokenService;

    public void save(ConfirmOrderSaveReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageRes<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("Pages: {}", req.getPage());
        LOG.info("Total: {}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("Rows: {}", pageInfo.getTotal());
        LOG.info("Pages: {}", pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);

        PageRes<ConfirmOrderQueryResp> pageResp = new PageRes<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    //@SentinelResource("done")
    @SentinelResource(value = "done", blockHandler = "doneBlock")
    public void done (ConfirmOrderMQDto dto) {
        //校验令牌余量
//        boolean validSkToken = skTokenService.validSkToken(req.getDate(), req.getTrainCode(), LoginMemberContext.getId());
//        if (validSkToken) {
//            LOG.info("Tokens are abundant");
//        } else {
//            LOG.info("No enough token");
//            throw new BusinessException(CONFIRM_ORDER_SK_TOKEN_FAIL);
//        }
        //redis 分布式锁解决超卖
        //redis key 要区分不同车次
        String lockKey = CONFIRM_ORDER + DateUtil.formatDate(dto.getDate()) + "-" + dto.getTrainCode();
        //即redis的setnx
        Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
        if (setIfAbsent) {
            LOG.info("Got lock");
        } else {
            //没抢到锁，其他线程正在购票，不需要抛出异常
            LOG.info("Fail to get lock");
            //throw new BusinessException(CONFIRM_ORDER_LOCK_FAIL);
            return;
        }
        //RLock lock = null;
        try {
            //lock = redissonClient.getLock(lockKey); //构造锁
            //boolean tryLock = lock.tryLock(0, TimeUnit.SECONDS); //带看门狗
           // boolean tryLock = lock.tryLock(0, 10, TimeUnit.SECONDS);//不带看门狗
//            if (tryLock) {
//                LOG.info("Got lock");
//
//            } else {
//                LOG.info("Fail to get lock");
//                throw new BusinessException(CONFIRM_ORDER_LOCK_FAIL);
//            }
            // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过

            // 保存确认订单表，状态初始
            while (true) {
                // 取确认订单表的记录，同日期车次，状态是I，分页处理，每次取N条
                ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                confirmOrderExample.setOrderByClause("id asc");
                ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
                criteria.andDateEqualTo(dto.getDate())
                        .andTrainCodeEqualTo(dto.getTrainCode())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                PageHelper.startPage(1, 5);
                List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);

                if (CollUtil.isEmpty(list)) {
                    LOG.info("No such order");
                    break;
                } else {
                    LOG.info("Handling {} orders", list.size());
                }

                // 一条一条地卖
                list.forEach(confirmOrder -> {
                    try {
                        sell(confirmOrder);
                    } catch (BusinessException e) {
                        //如果没票，继续售卖下一个订单
                        if (e.getE().equals(CONFIRM_ORDER_TICKET_COUNT_ERROR)) {
                            LOG.info("No more tickets, handle next order");
                            confirmOrder.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                            updateStatus(confirmOrder);
                        } else {
                            throw e;
                        }
                    }
                });
            }

            //stringRedisTemplate.delete(lockKey);
//        } catch (InterruptedException e) {
//            LOG.error("Fail to purchase", e);
        } finally {
            LOG.info("Purchase finishes, release the lock");
            stringRedisTemplate.delete(lockKey);
//            if (null != lock && lock.isHeldByCurrentThread()) lock.unlock(); //lock空或不是当前线程锁
        }

    }

    public void sell(ConfirmOrder confirmOrder) {
        ConfirmOrderDoneReq req = new ConfirmOrderDoneReq();
        req.setMemberId(confirmOrder.getMemberId());
        req.setDate(confirmOrder.getDate());
        req.setTrainCode(confirmOrder.getTrainCode());
        req.setDeparture(confirmOrder.getDeparture());
        req.setDestination(confirmOrder.getDestination());
        req.setDailyTrainTicketId(confirmOrder.getDailyTrainTicketId());
        req.setTickets(JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketReq.class));
        req.setImageCode("");
        req.setImageCodeToken("");
        req.setLogId("");

        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过

        // 将订单设置成处理中，避免重复处理
        LOG.info("Set order status to processing, {}", confirmOrder.getId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.PENDING.getCode());
        updateStatus(confirmOrder);

        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String departure = req.getDeparture();
        String destination = req.getDestination();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();

//            ConfirmOrder confirmOrder = new ConfirmOrder();
//            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
//            confirmOrder.setCreateTime(now);
//            confirmOrder.setUpdateTime(now);
//            confirmOrder.setDate(date);
//            confirmOrder.setMemberId(req.getMemberId());
//            confirmOrder.setTrainCode(trainCode);
//            confirmOrder.setDeparture(departure);
//            confirmOrder.setDestination(destination);
//            confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
//            confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
//            confirmOrder.setTickets(JSON.toJSONString(tickets));
//            confirmOrderMapper.insert(confirmOrder);

//        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
//        confirmOrderExample.setOrderByClause("id asc");
//        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
//        criteria.andDateEqualTo(req.getDate())
//                .andTrainCodeEqualTo(req.getTrainCode())
//                .andMemberIdEqualTo(req.getMemberId())
//                .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
//        List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);
//        ConfirmOrder confirmOrder;
//        if (CollUtil.isEmpty(list)) {
//            LOG.info("Fail to get order");
//            return true;
//        } else {
//            LOG.info("Handled with {} orders", list.size());
//            confirmOrder = list.get(0);
//        }
//
//        LOG.info("Generation finished");
        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, departure, destination);
        LOG.info("Remaining tickets: ", dailyTrainTicket);
        // 扣减余票数量，并判断余票是否足够
        reduceTickets(req, dailyTrainTicket);

        //最终选座结果
        List<DailyTrainSeat> finalSeatList = new ArrayList<>();

        // 计算相对第一个座位的偏移值。c1,d2对应[0,5]
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            LOG.info("可选座");
            //查出一等座还是二等座
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            LOG.info("Seat type: {}", colEnumList);
            //生成两排列表
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            LOG.info("Seat: {}", referSeatList);
            List<Integer> offset = new ArrayList<>();
            List<Integer> absoluteOffset = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                absoluteOffset.add(index);
            }
            LOG.info("Absolute Offset: {}", absoluteOffset);
            for (Integer idx : absoluteOffset) {
                int absOffset = idx - absoluteOffset.get(0);
                offset.add(absOffset);
            }
            LOG.info("Offset: {}", offset);
            getSeat(finalSeatList, date, trainCode, ticketReq0.getSeatTypeCode(),
                    ticketReq0.getSeat().split("")[0], offset,
                    dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
        } else {
            LOG.info("未选座");
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                getSeat(finalSeatList, date, trainCode, ticketReq.getSeatTypeCode(),
                        null, null,
                        dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
            }
            LOG.info("Final seat list: {}", finalSeatList);
            try {
                afterConfirmOrder.afterConfirm(dailyTrainTicket, finalSeatList, tickets, confirmOrder);
            } catch (Exception e) {
                LOG.error("Fail to save order: ", e);
                throw new BusinessException(CONFIRM_ORDER_EXCEPTION);
            }
        }
    }

    public void updateStatus(ConfirmOrder confirmOrder) {
        ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
        confirmOrderForUpdate.setId(confirmOrder.getId());
        confirmOrderForUpdate.setUpdateTime(new Date());
        confirmOrderForUpdate.setStatus(confirmOrder.getStatus());
        confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);
    }

    //选座。选座则一次性选完；未选座则一个个选
    private void getSeat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType,
                         String column, List<Integer> offsetList,
                         Integer startIndex, Integer endIndex) {
        List<DailyTrainSeat> getSeatList = new ArrayList<>();
        // 一个车厢一个车厢的获取座位数据
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectByType(date, trainCode, seatType);
        LOG.info("Get {} carriages according to tickets.", carriageList.size());

        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            getSeatList = new ArrayList<>();
            List<DailyTrainSeat> seatList = dailyTrainSeatService.selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            LOG.info("{} seats in carriage {}", seatList.size(), dailyTrainCarriage.getIndex());
            for (DailyTrainSeat dailyTrainSeat : seatList) {
                Integer seatIndex = dailyTrainSeat.getCarriageSeatIndex();
                //判断column
                String col = dailyTrainSeat.getCol();

                //判断有没有被选中过
                boolean alreadyChosen = false;
                for (DailyTrainSeat finalSeat : finalSeatList) {
                    if (finalSeat.getId().equals(dailyTrainSeat.getId())) {
                        alreadyChosen = true;
                        break;
                    }
                }
                if (alreadyChosen) continue;
                if (StrUtil.isBlank(column)) {
                    LOG.info("Not chosen");
                } else {
                    if (!column.equals(col)) {
                        LOG.info("current column: {}, target column: {}", col, column);
                        continue;
                    }
                }
                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    getSeatList.add(dailyTrainSeat);
                    LOG.info("Chosen");
                } else {
                    continue;
                }
                //根据offset选剩下的座位
                boolean isGetAllOffSetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    //有偏移值，校验是否可选
                    //找到偏移后的座位
                    for (int i = 1; i < offsetList.size(); i++) {
                        Integer offset = offsetList.get(i);
                        //座位索引从1开始
                        int nextIndex = seatIndex + offset - 1;

                        //选座一定在一个车厢
                        if (nextIndex >= seatList.size()) {
                            isGetAllOffSetSeat = false;
                            break;
                        }
                        DailyTrainSeat nextSeat = seatList.get(nextIndex);
                        boolean isChooseNext = calSell(nextSeat, startIndex, endIndex);
                        if (isChooseNext) {
                            LOG.info("Chosen {}", nextSeat.getCarriageSeatIndex());
                            getSeatList.add(nextSeat);
                        } else {
                            LOG.info("{} cannot be chosen", nextSeat.getCarriageSeatIndex());
                            isGetAllOffSetSeat = false;
                            break;
                        }
                    }
                }
                if (!isGetAllOffSetSeat) {
                    getSeatList = new ArrayList<>(); //清空列表
                    continue;
                }

                //保存选好的座位
                finalSeatList.addAll(getSeatList);
                return;
            }
        }
    }

    // 计算是否可卖
    //构造本次购票造成的售卖信息，和原售卖信息按位与
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        //如10001
        String selling = dailyTrainSeat.getSelling();
        //如 000
        String sellPart = selling.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0) {
            LOG.info("Seats {} from {} to {} in this train have been sold.", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            LOG.info("Seats {} from {} to {} in this train can be chosen.", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            // 111
            String curSell = sellPart.replace('0', '1');
            // 0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110
            curSell = StrUtil.fillAfter(curSell, '0', selling.length());
            //按位与 11111, 32
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(selling);
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            newSell = StrUtil.fillBefore(newSell, '0', selling.length());
            LOG.info("Final selling: {}", newSell);
            dailyTrainSeat.setSelling(newSell);
            return true;
        }
    }

    private static void reduceTickets(ConfirmOrderDoneReq req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketReq ticketReq : req.getTickets()) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            switch (seatTypeEnum) {
                case FC -> {
                    int countLeft = dailyTrainTicket.getFc() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setFc(countLeft);
                }
                case SC -> {
                    int countLeft = dailyTrainTicket.getSc() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setSc(countLeft);
                }
                case SS -> {
                    int countLeft = dailyTrainTicket.getSs() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setSs(countLeft);
                }
                case HS -> {
                    int countLeft = dailyTrainTicket.getHs() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setHs(countLeft);
                }
            }
        }
    }

    public void doneBlock(ConfirmOrderDoneReq req, BlockException e) {
        LOG.info("Order is restricted: {}", req);
        throw new BusinessException(CONFIRM_ORDER_FLOW_EXCEPTION);
    }

    public Integer queryLineCount(Long id) {
        ConfirmOrder confirmOrder = confirmOrderMapper.selectByPrimaryKey(id);
        ConfirmOrderStatusEnum statusEnum = EnumUtil.getBy(ConfirmOrderStatusEnum::getCode, confirmOrder.getStatus());
        int result = switch (statusEnum) {
            case PENDING -> 0;
            case SUCCESS -> -1; //正数预留为result的第几名
            case FAILURE -> -2;
            case EMPTY -> -3;
            case CANCEL -> -4;
            case INIT -> 999;
        };

        if (result == 999) {
            // 排在第几位，下面的写法：where a=1 and (b=1 or c=1) 等价于 where (a=1 and b=1) or (a=1 and c=1)
            ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
            confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                    .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                    .andCreateTimeLessThan(confirmOrder.getCreateTime())
                    .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
            confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                    .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                    .andCreateTimeLessThan(confirmOrder.getCreateTime())
                    .andStatusEqualTo(ConfirmOrderStatusEnum.PENDING.getCode());
            return Math.toIntExact(confirmOrderMapper.countByExample(confirmOrderExample));
        } else return result;
    }

    public Integer cancel(Long id) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        criteria.andIdEqualTo(id).andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setStatus(ConfirmOrderStatusEnum.CANCEL.getCode());
        return confirmOrderMapper.updateByExampleSelective(confirmOrder, confirmOrderExample);
    }
}
