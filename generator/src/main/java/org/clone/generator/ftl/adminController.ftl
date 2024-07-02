package org.clone.${module}.controller.admin;

import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.${module}.req.${Domain}QueryReq;
import org.clone.${module}.req.${Domain}SaveReq;
import org.clone.${module}.resp.${Domain}QueryResp;
import org.clone.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/${do_main}")
public class ${Domain}AdminController {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {
        PageRes<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new CommonResponse<>();
    }
}
