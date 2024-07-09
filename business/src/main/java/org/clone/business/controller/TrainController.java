package org.clone.business.controller;

import jakarta.annotation.Resource;
import org.clone.business.resp.TrainQueryResp;
import org.clone.business.service.TrainService;
import org.clone.common.response.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @GetMapping("query-all")
    public CommonResponse<List<TrainQueryResp>> queryAll() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResponse<>(list);
    }

}
