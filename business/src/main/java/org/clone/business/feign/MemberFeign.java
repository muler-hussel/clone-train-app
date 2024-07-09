package org.clone.business.feign;

import org.clone.common.request.MemberTicketReq;
import org.clone.common.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member", url = "http://127.0.0.1:8001")
public interface MemberFeign {

    @GetMapping("member/feign/ticket/save")
    CommonResponse<Object> save(@RequestBody MemberTicketReq req);
}
