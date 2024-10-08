package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.TrainStationQueryReq;
import org.clone.business.req.TrainStationSaveReq;
import org.clone.business.resp.TrainStationQueryResp;
import org.clone.business.service.TrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageRes<TrainStationQueryResp> list = trainStationService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return new CommonResponse<>();
    }
}
