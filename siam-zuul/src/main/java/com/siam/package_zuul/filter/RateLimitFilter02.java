//package com.siam.package_zuul.filter;
//
//import com.google.common.util.concurrent.RateLimiter;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.springframework.cloud.netflix.zuul.filters.Route;
//import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Component
//public class RateLimitFilter02 extends ZuulFilter {
//
//    // 使用RateLimiter创建令牌桶实例
//    private static final RateLimiter URL_RATE_LIMITER = RateLimiter.create(10); // 每分钟允许调用10次
//    private static final RateLimiter IP_RATE_LIMITER = RateLimiter.create(100); // 每分钟允许请求100次
//    private static final RateLimiter USER_GROUP_RATE_LIMITER = RateLimiter.create(50); // 每分钟允许调用50次
//
//    private final RouteLocator routeLocator;
//
//    public RateLimitFilter02(RouteLocator routeLocator) {
//        this.routeLocator = routeLocator;
//    }
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//
//        // 获取请求URL及相关信息
//        String url = request.getRequestURI();
//        String clientIP = getClientIP(request);
//        String userId = getUIDFromRequest(request);
//
//        Route route = routeLocator.getMatchingRoute(url);
//
//        // 对请求的目标URL进行限流
//        if (route != null && !URL_RATE_LIMITER.tryAcquire()) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
//            context.setResponseBody("Request limit exceeded for URL");
//            return null;
//        }
//
//        // 对客户端IP进行限流
//        if (!IP_RATE_LIMITER.tryAcquire()) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
//            context.setResponseBody("Request limit exceeded for IP");
//            return null;
//        }
//
//        // 对特定用户或用户组进行限流
//        if (!isVIPUser(userId) && !USER_GROUP_RATE_LIMITER.tryAcquire()) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
//            context.setResponseBody("Request limit exceeded for User/Group");
//            return null;
//        }
//
//        return null;
//    }
//
//    // 从HttpServletRequest中获取客户端真实的IP地址
//    private String getClientIP(HttpServletRequest request) {
//        // 实现略，根据实际情况获取客户端IP地址
//        return "";
//    }
//
//    // 根据请求中的信息判断是否为VIP用户
//    private boolean isVIPUser(String userId) {
//        // 实现略，根据实际情况判断用户是否为VIP
//        return false;
//    }
//
//    // 从请求中获取用户ID
//    private String getUIDFromRequest(HttpServletRequest request) {
//        // 实现略，根据实际情况获取用户ID
//        return "";
//    }
//}