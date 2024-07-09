package org.clone.member.feign;

import jakarta.validation.Valid;
import org.clone.common.request.MemberTicketReq;
import org.clone.common.response.CommonResponse;
import org.clone.member.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/ticket")
public class FeignTicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/save")
    public CommonResponse<Object> save (@Valid @RequestBody MemberTicketReq req) {
        ticketService.save(req);
        return new CommonResponse<>();
    }
}
