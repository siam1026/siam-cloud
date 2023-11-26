package com.siam.package_zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.filter.StoneFilter;
import com.siam.package_common.util.BeanUtils;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_common.util.RedisUtils;
import com.siam.package_zuul.config.WebMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 用户过滤器
 *
 * @author 暹罗
 */
@Slf4j
@Component
public class EncryptFilter extends ZuulFilter implements StoneFilter {

    @Autowired
    private WebMvcConfig webMvcConfig;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 过滤类型
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    /**
     * 过滤器执行顺序，当一个请求在同一个阶段存在多个过滤器的时候，多个过滤器执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断过滤器是否生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //此处只需要把当前请求路径  扔到我公共方法中检测是否需要拦截即可
        //通过当前上下文 获取 Request、Response对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        /*System.out.println("\nuri = " + request.getRequestURI());
        System.out.println("\nurl = " + request.getRequestURL());*/

        return webMvcConfig.matches(request.getRequestURI());
    }

    @Override
    public Object run() throws ZuulException {
        //如果开启了数据加密功能，则对返回数据进行加密处理
        Boolean isEncrypt = (Boolean) JsonUtils.toObject(redisUtils.get("isEncrypt").toString(), Boolean.class);
        if(isEncrypt!=null && isEncrypt){
            //通过当前上下文 获取 Request、Response对象
            RequestContext currentContext = RequestContext.getCurrentContext();
            InputStream inputStream = currentContext.getResponseDataStream();
            try {
                //获取原数据
                String body = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
                //加密返回结果
                String resultBody = this.encrypt(body);
                currentContext.setResponseBody(resultBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加密返回结果
     *
     * @param body
     * @return
     * @author 暹罗
     */
    private String encrypt(String body){
        //加密规则：
        //1、根据字段名加密
        //2、浮点数全部加密
        //3、所有字段值全部加密
        BasicResult basicResult = (BasicResult) JsonUtils.toObject(body, BasicResult.class);
        Map<String, Object> data = (Map) basicResult.getData();
        if(data.containsKey("records")){
            //列表数据
            List<Map<String, Object>> records;
            try{
                 records = (List) data.get("records");
                for (Map<String, Object> map : records) {
                    //渲染Map数据
                    renderMap(map);
                }
            }catch (Exception ex){
                records = BeanUtils.objectsToMaps((List) data.get("records"));
                for (Map<String, Object> map : records) {
                    //渲染Map数据
                    renderMap(map);
                }
            }
        }else{
            //详情等数据
            //渲染Map数据
            renderMap(data);
        }

        return JsonUtils.toJson(basicResult);
    }

    /**
     * 渲染Map数据
     *
     * @param map
     * @return
     * @author 暹罗
     */
    private void renderMap(Map<String, Object> map){
        for (String key : map.keySet()) {
            if(key.equals("id") || key.endsWith("Id")
                || key.equals("type") || key.endsWith("Type")
                || key.equals("status") || key.endsWith("Status")
                || key.startsWith("is")){
                continue;
            }
            if(map.get(key) instanceof String){
                map.put(key, "****");
            }else if(map.get(key) instanceof Integer || map.get(key) instanceof Double || map.get(key) instanceof BigDecimal){
                map.put(key, 0);
            }
        }
    }
}