package org.clone.business.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.DailyTrainStationQueryAllReq;
import org.clone.business.resp.DailyTrainStationQueryResp;
import org.clone.business.service.DailyTrainStationService;
import org.clone.common.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/daily-train-station")
public class DailyTrainStationController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @GetMapping("/query-by-train-code")
    public CommonResponse<List<DailyTrainStationQueryResp>> queryByTrain(@Valid DailyTrainStationQueryAllReq req) {
        List<DailyTrainStationQueryResp> list = dailyTrainStationService.queryByTrain(req.getDate(), req.getTrainCode());
        return new CommonResponse<>(list);
    }
}
