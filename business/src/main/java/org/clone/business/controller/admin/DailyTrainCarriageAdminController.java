package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.DailyTrainCarriageQueryReq;
import org.clone.business.req.DailyTrainCarriageSaveReq;
import org.clone.business.resp.DailyTrainCarriageQueryResp;
import org.clone.business.service.DailyTrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody DailyTrainCarriageSaveReq req) {
        dailyTrainCarriageService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<DailyTrainCarriageQueryResp>> queryList(@Valid DailyTrainCarriageQueryReq req) {
        PageRes<DailyTrainCarriageQueryResp> list = dailyTrainCarriageService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        dailyTrainCarriageService.delete(id);
        return new CommonResponse<>();
    }
}
