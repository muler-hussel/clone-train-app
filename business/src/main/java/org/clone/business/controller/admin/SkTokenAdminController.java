package org.clone.business.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.business.req.SkTokenQueryReq;
import org.clone.business.req.SkTokenSaveReq;
import org.clone.business.resp.SkTokenQueryResp;
import org.clone.business.service.SkTokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sk-token")
public class SkTokenAdminController {

    @Resource
    private SkTokenService skTokenService;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody SkTokenSaveReq req) {
        skTokenService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<SkTokenQueryResp>> queryList(@Valid SkTokenQueryReq req) {
        PageRes<SkTokenQueryResp> list = skTokenService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        skTokenService.delete(id);
        return new CommonResponse<>();
    }
}
