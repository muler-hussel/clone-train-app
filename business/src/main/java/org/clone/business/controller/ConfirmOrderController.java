package org.clone.business.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.ConfirmOrderDoneReq;
import org.clone.business.service.ConfirmOrderService;
import org.clone.common.response.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/done")
    public CommonResponse<Object> done (@Valid @RequestBody ConfirmOrderDoneReq req) {
        confirmOrderService.done(req);
        return new CommonResponse<>();
    }
}
