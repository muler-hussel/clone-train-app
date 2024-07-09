package org.clone.business.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.DailyTrainTicketQueryReq;
import org.clone.business.resp.DailyTrainTicketQueryResp;
import org.clone.business.service.DailyTrainTicketService;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/daily-train-ticket")
public class DailyTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public CommonResponse<PageRes<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageRes<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResponse<>(list);
    }
}
