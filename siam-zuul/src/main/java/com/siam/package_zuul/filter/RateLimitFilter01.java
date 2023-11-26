//package com.siam.package_zuul.filter;
//
//import com.google.common.util.concurrent.RateLimiter;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
//
///**
// * @Classname RateLimiter
// * @Created by Michael
// * @Date 2023/7/13
// * @Description 限流过滤
// */
//@Component
//public class RateLimitFilter01 extends ZuulFilter {
//
//    //令牌桶
//    //第一个参数，是总令牌数
//    //第二个参数，简单理解为多久放令牌
//    //第三个参数，是时间单位，即多久投放依次令牌
//    static RateLimiter rateLimiter = RateLimiter.create(8, 2, TimeUnit.SECONDS);
//
//    @Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return SERVLET_DETECTION_FILTER_ORDER - 1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        // 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false，你懂的。
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext context = RequestContext.getCurrentContext();
//        boolean allowed = rateLimiter.tryAcquire();
//        if (!allowed) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(501);
//        }
//        return null;
//    }
//}