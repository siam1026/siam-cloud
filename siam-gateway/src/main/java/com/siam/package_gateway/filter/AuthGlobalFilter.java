package com.siam.package_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局认证过滤器 - 检查token是否存在
 * （具体的session验证由各业务服务自行处理）
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        String method = request.getMethod().name();

        // OPTIONS请求直接放行（跨域预检）
        if ("OPTIONS".equals(method)) {
            return chain.filter(exchange);
        }

        // 登录、注册等公开路径放行
        if (isPublicPath(uri)) {
            return chain.filter(exchange);
        }

        // 从Header或参数中获取token
        String token = extractToken(request);
        if (token == null || token.isEmpty()) {
            log.debug("Token missing for URI: {}", uri);
            return unauthorizedResponse(exchange, "您暂未登录，请登陆后访问");
        }

        // Token存在，放行（具体验证由下游服务处理）
        // 将token传递给下游服务
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-Auth-Token", token)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private String extractToken(ServerHttpRequest request) {
        // 先从Header获取
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && !token.isEmpty()) {
            // 如果是Bearer格式，去掉前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return token;
        }

        // 从请求参数获取
        return request.getQueryParams().getFirst("token");
    }

    private boolean isPublicPath(String uri) {
        return uri.contains("/login")
                || uri.contains("/register")
                || uri.contains("/swagger")
                || uri.contains("/v3/api-docs")
                || uri.contains("/actuator")
                || uri.contains("/notify")
                || uri.contains("/callback")
                || uri.contains("/pay")
                || uri.contains("/Pay")
                || uri.contains("/shop");
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = "{\"success\":false,\"message\":\"" + message + "\"}";
        byte[] bytes = body.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
