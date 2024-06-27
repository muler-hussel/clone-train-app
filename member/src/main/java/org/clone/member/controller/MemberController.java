package org.clone.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.member.request.MemberLoginReq;
import org.clone.member.request.MemberSendCodeReq;
import org.clone.member.service.MemberService;
import org.clone.member.request.MemberRegisterReq;
import org.clone.common.response.CommonResponse;
import org.clone.member.response.MemberLoginRes;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResponse<Integer> count() {
        int count = memberService.count();
        CommonResponse<Integer> commonResp = new CommonResponse<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResponse<Long> register(@Valid MemberRegisterReq req) {
        long register = memberService.register(req);
        return new CommonResponse<>(register);
    }

    @PostMapping("/send-code")
    public CommonResponse<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResponse<>();
    }

    @PostMapping("/login")
    public CommonResponse<MemberLoginRes> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginRes res = memberService.login(req);
        return new CommonResponse<>(res);
    }

}
