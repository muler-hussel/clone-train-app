package org.clone.controller;

import jakarta.annotation.Resource;
import org.clone.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @PostMapping("/register")
    public long register(String mobile) {
        return memberService.register(mobile);
    }

}
