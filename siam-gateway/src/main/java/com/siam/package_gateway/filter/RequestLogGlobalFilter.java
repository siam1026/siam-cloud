package com.siam.package_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求日志过滤器
 */
@Slf4j
@Component
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().name();

        // OPTIONS跨域预检请求不记录日志
        if ("OPTIONS".equals(method)) {
            return chain.filter(exchange);
        }

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        exchange.getAttributes().put("REQUEST_START_TIME", startTime);

        log.info("-------------------REQUEST-START-----------------");
        log.info("URL: [{}]", request.getURI());
        log.info("HTTP-METHOD: [{}]", method);
        log.info("Content-type: [{}]", request.getHeaders().getContentType());

        return chain.filter(exchange).doFinally(signalType -> {
            Long startTimeAttr = exchange.getAttribute("REQUEST_START_TIME");
            if (startTimeAttr != null) {
                long duration = System.currentTimeMillis() - startTimeAttr;
                log.info("Request Spend Time: [{}ms]", duration);
            }
            log.info("-------------------REQUEST-END-----------------");
        });
    }

    @Override
    public int getOrder() {
        return -90;
    }
}
