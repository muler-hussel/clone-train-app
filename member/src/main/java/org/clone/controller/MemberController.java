package org.clone.controller;

import jakarta.annotation.Resource;
import org.clone.request.MemberRegisterReq;
import org.clone.service.MemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.clone.response.CommonResponse;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/register")
    public CommonResponse<Long> register(@Validated MemberRegisterReq req) {
        long register = memberService.register(req);
        return new CommonResponse<>(register);
    }

}
