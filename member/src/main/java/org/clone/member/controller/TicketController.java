package org.clone.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.member.req.TicketQueryReq;
import org.clone.member.resp.TicketQueryResp;
import org.clone.member.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResponse<PageRes<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        CommonResponse<PageRes<TicketQueryResp>> commonResponse = new CommonResponse<>();
        req.setMemberId(LoginMemberContext.getId());
        PageRes<TicketQueryResp> list = ticketService.queryList(req);
        commonResponse.setContent(list);
        return commonResponse;
    }
}
