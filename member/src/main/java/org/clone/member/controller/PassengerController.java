package org.clone.member.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.common.context.LoginMemberContext;
import org.clone.common.response.CommonResponse;
import org.clone.common.response.PageRes;
import org.clone.member.request.PassengerQueryReq;
import org.clone.member.request.PassengerSaveRequest;
import org.clone.member.response.PassengerQueryRes;
import org.clone.member.service.PassengerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResponse<Object> register(@Valid @RequestBody PassengerSaveRequest req) {
        passengerService.save(req);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageRes<PassengerQueryRes>> queryList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageRes<PassengerQueryRes> list = passengerService.queryList(req);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete (@PathVariable Long id) {
        passengerService.delete(id);
        return new CommonResponse<>();
    }

    @GetMapping("/query-mine")
    public CommonResponse<List<PassengerQueryRes>> queryMime() {
        List<PassengerQueryRes> list = passengerService.queryMine();
        return new CommonResponse<>(list);
    }
}
