package org.clone.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.ConfirmOrderQueryReq;
import org.clone.business.req.ConfirmOrderSaveReq;
import org.clone.business.resp.ConfirmOrderQueryResp;
import org.clone.business.service.ConfirmOrderService;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/confirm-order")
public class ConfirmOrderAdminController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody ConfirmOrderSaveReq req) {
        confirmOrderService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<ConfirmOrderQueryResp>> queryList(@Valid ConfirmOrderQueryReq req) {
        PageRes<ConfirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        confirmOrderService.delete(id);
        return new CommonResponse<>();
    }
}
