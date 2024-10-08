package org.clone.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.DailyTrainQueryReq;
import org.clone.business.req.DailyTrainSaveReq;
import org.clone.business.resp.DailyTrainQueryResp;
import org.clone.business.service.DailyTrainService;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {

    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody DailyTrainSaveReq req) {
        dailyTrainService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<DailyTrainQueryResp>> queryList(@Valid DailyTrainQueryReq req) {
        PageRes<DailyTrainQueryResp> list = dailyTrainService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return new CommonResponse<>();
    }

    @GetMapping("/gen-daily/{date}")
    public CommonResponse<Object> queryList(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return new CommonResponse<>();
    }
}
