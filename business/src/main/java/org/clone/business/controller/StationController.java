package org.clone.business.controller;

import jakarta.annotation.Resource;
import org.clone.business.resp.StationQueryResp;
import org.clone.business.service.StationService;
import org.clone.common.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {

    @Resource
    private StationService stationService;

    @GetMapping("/query-all")
    public CommonResponse<List<StationQueryResp>> queryAll() {
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResponse<>(list);
    }
}
