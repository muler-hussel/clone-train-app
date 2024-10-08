package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.TrainSeatQueryReq;
import org.clone.business.req.TrainSeatSaveReq;
import org.clone.business.resp.TrainSeatQueryResp;
import org.clone.business.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageRes<TrainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return new CommonResponse<>();
    }
}
