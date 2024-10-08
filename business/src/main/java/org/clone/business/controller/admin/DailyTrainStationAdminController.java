package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.DailyTrainStationQueryReq;
import org.clone.business.req.DailyTrainStationSaveReq;
import org.clone.business.resp.DailyTrainStationQueryResp;
import org.clone.business.service.DailyTrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<DailyTrainStationQueryResp>> queryList(@Valid DailyTrainStationQueryReq req) {
        PageRes<DailyTrainStationQueryResp> list = dailyTrainStationService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return new CommonResponse<>();
    }
}
