package org.clone.business.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.business.req.StationQueryReq;
import org.clone.business.req.StationSaveReq;
import org.clone.business.resp.StationQueryResp;
import org.clone.business.service.StationService;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageRes<StationQueryResp> list = stationService.queryList(req);
        return new CommonResponse<>(list);
    }

    @GetMapping("/query-all")
    public CommonResponse<List<StationQueryResp>> queryAll() {
        List<StationQueryResp> list = stationService.queryAll();
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        stationService.delete(id);
        return new CommonResponse<>();
    }
}
