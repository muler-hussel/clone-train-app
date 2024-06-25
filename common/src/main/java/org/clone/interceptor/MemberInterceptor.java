package org.clone.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.clone.context.LoginMemberContext;
import org.clone.response.MemberLoginRes;
import org.clone.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MemberInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MemberInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            LOG.info("Get token: {}", token);
            JSONObject loginMember = JwtUtil.getJSONObject(token);
            LOG.info("Current member: {}", loginMember);
            LoginMemberContext.setMember(JSONUtil.toBean(loginMember, MemberLoginRes.class));
        }
        return true;
    }
}
