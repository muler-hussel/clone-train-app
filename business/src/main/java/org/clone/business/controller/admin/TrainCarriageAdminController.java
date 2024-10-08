package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.TrainCarriageQueryReq;
import org.clone.business.req.TrainCarriageSaveReq;
import org.clone.business.resp.TrainCarriageQueryResp;
import org.clone.business.service.TrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody TrainCarriageSaveReq req) {
        trainCarriageService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req) {
        PageRes<TrainCarriageQueryResp> list = trainCarriageService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
        return new CommonResponse<>();
    }
}
