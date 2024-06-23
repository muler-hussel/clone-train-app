package org.clone.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.request.MemberLoginReq;
import org.clone.request.MemberRegisterReq;
import org.clone.request.MemberSendCodeReq;
import org.clone.response.CommonResponse;
import org.clone.response.MemberLoginRes;
import org.clone.service.MemberService;
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
