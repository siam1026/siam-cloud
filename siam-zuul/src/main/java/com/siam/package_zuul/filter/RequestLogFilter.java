package com.siam.package_zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
 
 
/**
 * 功能描述: <br>
 * 〈请求参数日志过滤器〉
 *
 * @Author:hanxinghua
 * @Date: 2020/7/4
 */
@Slf4j
@Component
public class RequestLogFilter extends ZuulFilter {
 
 
    /**
     * 返回过滤器的类型
     * 类型：pre、route、post、error等
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
 
    /**
     * 返回一个int值来指定同一类型过滤器的执行顺序，不同的过滤器允许返回相同的数字
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }
 
    /**
     * 返回一个boolean值来判断该过滤器是否需要执行
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }
 
    /**
     * 过滤器的具体逻辑
     *
     * @return
     */
    @Override
    public Object run() {
        // 记录PRE_TYPE Filter开始时间戳
        long startTime = System.currentTimeMillis();
        RequestContext.getCurrentContext().set("startTime", startTime);

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest serverHttpRequest = currentContext.getRequest();
        String method = serverHttpRequest.getMethod();
        // 处理跨域发送两次请求，打印两遍日志问题
        if (Constant.OPTIONS.equals(method)) {
            return null;
        }
        log.info("-------------------REQUEST-START-----------------");
        // 记录下请求内容
        log.info("URL: [{}]", serverHttpRequest.getRequestURL().toString());
        log.info("HTTP-METHOD: [{}]", method);
        log.info("Content-type: [{}]", serverHttpRequest.getHeader(Constant.CONTENT_TYPE));
        log.info("AUTHORIZATION: [{}]", serverHttpRequest.getHeader(Constant.AUTHORIZATION));
        if (Constant.APP_JSON.equals(serverHttpRequest.getHeader(Constant.CONTENT_TYPE))) {
//            //记录application/json时的传参
//            HttpServletRequestWrapper httpServletRequestWrapper = (HttpServletRequestWrapper) serverHttpRequest;
//            /*BodyReaderHttpServletRequestWrapper wrapper = (BodyReaderHttpServletRequestWrapper) httpServletRequestWrapper.getRequest();*/
//            BodyReaderHttpServletRequestWrapper wrapper = null;
//            try {
//                wrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequestWrapper.getRequest());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            log.info("Request Parameters: \n [{}]", wrapper.getBody());

            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = serverHttpRequest.getReader();
                char[] buffer = new char[1024];
                int length;
                while ((length = reader.read(buffer)) != -1) {
                    requestBody.append(buffer, 0, length);
                }
            } catch (IOException e) {
                // 处理异常
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // 处理异常
                        e.printStackTrace();
                    }
                }
            }
            log.info("Request Parameters: \n [{}]", requestBody.toString());
        } else {
            //记录请求的键值对
            log.info("Request Parameters: \n [{}]", handleInputParameters(serverHttpRequest.getParameterMap()));
        }
        return null;
 
    }
 
    /**
     * 处理参数
     *
     * @param map
     * @return
     */
    private  String handleInputParameters(Map<String, String[]> map){
        if (map.isEmpty()) {
            return null;
        }
        return JSON.toJSONString(map);
    }
 
}