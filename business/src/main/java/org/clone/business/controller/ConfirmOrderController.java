package org.clone.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.ConfirmOrderDoneReq;
import org.clone.business.service.BeforeConfirmOrderService;
import org.clone.business.service.ConfirmOrderService;
import org.clone.common.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static org.clone.common.exception.BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    private Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);
    @Resource
    private ConfirmOrderService confirmOrderService;

    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Value("${spring.profiles.active}")
    private String env;

    //接口value不能和路径一样，会导致限流走不到降级方法中
    @PostMapping("/done")
    @SentinelResource(value = "confirmOrderDone", blockHandler = "doneConfirmBlock")
    public CommonResponse<Object> done (@Valid @RequestBody ConfirmOrderDoneReq req) {
        if (!env.equals("dev")) { //为了压测，开发环境不需要图形验证码
            String imageCodeToken = req.getImageCodeToken();
            String imageCode = req.getImageCode();
            String imageCodeRedis = redisTemplate.opsForValue().get(imageCodeToken);
            LOG.info("Code: {}", imageCodeRedis);
            if (ObjectUtils.isEmpty(imageCodeRedis)) {
                return new CommonResponse<>(false, "Code expires", null);
            }
            // 验证码校验，大小写忽略，提升体验，比如Oo Vv Ww容易混
            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
                return new CommonResponse<>(false, "Code is not correct", null);
            } else {
                // 验证通过后，移除验证码
                redisTemplate.delete(imageCodeToken);
            }
        }

        Long id = beforeConfirmOrderService.beforeConfirmOrderService(req);
        return new CommonResponse<>(String.valueOf(id));
    }

    //查排队的数量
    @GetMapping("/query-line-count/{id}")
    public CommonResponse<Integer> queryLineCount(@PathVariable Long id) {
        Integer count = confirmOrderService.queryLineCount(id);
        return new CommonResponse<>(count);
    }
    public CommonResponse<Object> doneConfirmBlock(ConfirmOrderDoneReq req, BlockException e) {
        LOG.info("Order is restricted: {}", req);
        //throw new BusinessException(CONFIRM_ORDER_FLOW_EXCEPTION);
        CommonResponse<Object> commonResponse = new CommonResponse<>();
        commonResponse.setSuccess(false);
        commonResponse.setMessage(CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return commonResponse;
    }
}
