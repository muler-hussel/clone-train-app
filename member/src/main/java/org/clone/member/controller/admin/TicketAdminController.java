package org.clone.member.controller.admin;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.member.req.TicketQueryReq;
import org.clone.member.resp.TicketQueryResp;
import org.clone.member.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageRes<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResponse<>(list);
    }
}
