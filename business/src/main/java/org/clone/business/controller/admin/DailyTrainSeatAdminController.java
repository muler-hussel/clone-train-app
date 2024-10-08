package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.DailyTrainSeatQueryReq;
import org.clone.business.req.DailyTrainSeatSaveReq;
import org.clone.business.resp.DailyTrainSeatQueryResp;
import org.clone.business.service.DailyTrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody DailyTrainSeatSaveReq req) {
        dailyTrainSeatService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<DailyTrainSeatQueryResp>> queryList(@Valid DailyTrainSeatQueryReq req) {
        PageRes<DailyTrainSeatQueryResp> list = dailyTrainSeatService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return new CommonResponse<>();
    }
}
