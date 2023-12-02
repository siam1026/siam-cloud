package com.siam.package_zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
 
 
/**
 * 功能描述: <br>
 * 〈响应参数日志过滤器〉
 *
 * @Author:hanxinghua
 * @Date: 2020/7/4
 */
@Slf4j
@Component
public class ResponseLogFilter extends ZuulFilter {
 
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
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
        try {
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletResponse response = currentContext.getResponse();

            //需要设置编码格式，否则前端拿到的中文数据会乱码变成???（暂时注释掉，因为加了这两句代码，swagger获取不到返回信息）
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            String method = currentContext.getRequest().getMethod();
            // 处理跨域发送两次请求，打印两遍日志问题
            if (Constant.OPTIONS.equals(method)) {
                return null;
            }
            List<String> contentTypeList = currentContext.getZuulResponseHeaders().stream().
                    filter(x -> x.first().equals(Constant.CONTENT_TYPE)).map(x -> x.second()).collect(Collectors.toList());
 
            if (contentTypeList != null && !contentTypeList.isEmpty()) {
                String contentType = contentTypeList.get(0);
                log.info("Response Content-type: [{}]", contentType);
 
                InputStream responseDataStream = currentContext.getResponseDataStream();
                if (responseDataStream != null && contentType !=null && contentType.contains(Constant.APP_JSON)) {
                    String body = read(responseDataStream);
                    responseDataStream.close();
                    log.info("Response Parameters: \n [{}]", body);
                    currentContext.setResponseBody(body); //这句话如果注释掉，返回内容直接会被清空
                }
            }

            // 获取PRE_TYPE Filter开始时间戳
            long startTime = (Long) RequestContext.getCurrentContext().get("startTime");

            // 记录POST_TYPE Filter结束时间戳
            long endTime = System.currentTimeMillis();

            // 计算PRE_TYPE和POST_TYPE Filter之间的请求耗时
            long duration = endTime - startTime;

            log.info("Request Spend Time: [{}]", duration + "ms");
 
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("-------------------REQUEST-END-----------------");

        return null;
    }

    private String read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}