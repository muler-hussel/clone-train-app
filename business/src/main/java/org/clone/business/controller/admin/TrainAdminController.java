package org.clone.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.TrainQueryReq;
import org.clone.business.req.TrainSaveReq;
import org.clone.business.resp.TrainQueryResp;
import org.clone.business.service.TrainSeatService;
import org.clone.business.service.TrainService;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {

    @Resource
    private TrainService trainService;

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageRes<TrainQueryResp> list = trainService.queryList(req);
        return new CommonResponse<>(list);
    }

    @GetMapping("query-all")
    public CommonResponse<List<TrainQueryResp>> queryAll() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResponse<>(list);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public CommonResponse<Object> genSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return new CommonResponse<>();
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        trainService.delete(id);
        return new CommonResponse<>();
    }
}
