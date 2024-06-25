package org.clone.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.clone.context.LoginMemberContext;
import org.clone.request.PassengerQueryReq;
import org.clone.request.PassengerSaveRequest;
import org.clone.response.CommonResponse;
import org.clone.response.PageRes;
import org.clone.response.PassengerQueryRes;
import org.clone.service.PassengerService;
import org.springframework.web.bind.annotation.*;

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
}
