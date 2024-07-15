package org.clone.batch.feign;

import org.clone.common.response.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BusinessFeignFallback implements BusinessFeign {

    @Override
    public String hello() {
        return "Fallback";
    }

    @Override
    public CommonResponse<Object> genDaily(Date date) {
        return null;
    }
}
