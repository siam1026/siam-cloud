package com.siam.package_zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RateLimitFilter extends ZuulFilter {

    private static final double URL_RATE_LIMIT = 10.0; // 每分钟允许调用10次
    private static final double IP_RATE_LIMIT = 100.0; // 每分钟允许请求100次
    private static final double USER_GROUP_RATE_LIMIT = 50.0; // 每分钟允许调用50次

    private final RouteLocator routeLocator;
    private final RateLimiter urlRateLimiter;
    private final RateLimiter ipRateLimiter;
    private final RateLimiter userGroupRateLimiter;

    @Autowired
    public RateLimitFilter(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
        this.urlRateLimiter = RateLimiter.create(URL_RATE_LIMIT);
        this.ipRateLimiter = RateLimiter.create(IP_RATE_LIMIT);
        this.userGroupRateLimiter = RateLimiter.create(USER_GROUP_RATE_LIMIT);
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String url = request.getRequestURI();
        String clientIP = getClientIP(request);
        String userId = getUserIdFromRequest(request);

        Route route = routeLocator.getMatchingRoute(url);

        // 对请求的目标URL进行限流
        if (route != null && !urlRateLimiter.tryAcquire()) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            context.setResponseBody("Request limit exceeded for URL");
            return null;
        }

        // 对客户端IP进行限流
        if (!ipRateLimiter.tryAcquire()) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            context.setResponseBody("Request limit exceeded for IP");
            return null;
        }

//        // 对特定用户或用户组进行限流
//        if (!isVIPUser(userId) && !userGroupRateLimiter.tryAcquire()) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
//            context.setResponseBody("Request limit exceeded for User/Group");
//            return null;
//        }

        return null;
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // X-Forwarded-For header can contain a comma-separated list of IPs if multiple proxies are involved.
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        // 根据实际情况获取用户ID
        return null;
    }

    private boolean isVIPUser(String userId) {
        // 根据实际情况判断用户是否为VIP
        return false;
    }
}