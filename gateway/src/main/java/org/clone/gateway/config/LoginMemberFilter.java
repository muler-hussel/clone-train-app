package org.clone.gateway.config;

import org.clone.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginMemberFilter implements Ordered, GlobalFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        //排除不需要拦截的请求
        if (path.contains("/admin")
                || path.contains("/redis")
                || path.contains("/hello")
                || path.contains("member/member/login")
                || path.contains("member/member/send-code")) {
            LOG.info("Do not need login validation: {}", path);
            return chain.filter(exchange);
        } else {
            LOG.info("Need login validation: {}", path);
        }
        //获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("Login validation starts, token: {}", token);
        if (token == null || token.isEmpty()) {
            LOG.info("Token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); //401
            return exchange.getResponse().setComplete();
        }
        //校验token是否有效
        boolean validate = JwtUtil.validate(token);
        if (validate) {
            LOG.info("Access");
            return chain.filter(exchange);
        } else {
            LOG.warn("Intercept");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
