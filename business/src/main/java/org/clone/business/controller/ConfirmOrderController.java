package org.clone.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.ConfirmOrderDoneReq;
import org.clone.business.service.ConfirmOrderService;
import org.clone.common.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.clone.common.exception.BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    private Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);
    @Resource
    private ConfirmOrderService confirmOrderService;

    //接口value不能和路径一样，会导致限流走不到降级方法中
    @PostMapping("/done")
    @SentinelResource(value = "confirmOrderDone", blockHandler = "doneConfirmBlock")
    public CommonResponse<Object> done (@Valid @RequestBody ConfirmOrderDoneReq req) {
        confirmOrderService.done(req);
        return new CommonResponse<>();
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
