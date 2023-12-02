package com.siam.package_zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.siam.package_common.constant.BaseCode;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.filter.StoneFilter;
import com.siam.package_common.util.JsonUtils;
import com.siam.package_user.auth.cache.MerchantSessionManager;
import com.siam.package_user.entity.Merchant;
import com.siam.package_user.util.TokenUtil;
import com.siam.package_zuul.config.WebMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商家过滤器
 *
 * @author 暹罗
 */
@Slf4j
@Component
public class MerchantFilter extends ZuulFilter implements StoneFilter {

    @Autowired
    private MerchantSessionManager adminSessionManager;

    @Autowired
    private WebMvcConfig webMvcConfig;

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
        return FilterConstants.PRE_TYPE;
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
        //通过当前上下文 获取 Request、Response对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();

        //从header或param里面获取token(暂时这样写，兼容之前前端的写法)
        String token = TokenUtil.getToken();
        log.debug("\ntoken = " + token);
        if(token == null){
            BasicResult basicResult = new BasicResult();
            basicResult.setSuccess(false);
            basicResult.setCode(BaseCode.TOKEN_ERR);
            basicResult.setMessage("您暂未登录，请登陆后访问");
            log.debug("\n" + JsonUtils.toJson(basicResult));

            //token验证失败，不会继续执行后续代码，网关服务直接响应给客户端
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody(JsonUtils.toJson(basicResult));
            currentContext.setResponseStatusCode(200);
            return null;
        }

        //TODO - 目前如果此处调用出错，该方法是不会继续执行，但是目标接口依旧是返回了数据
        Merchant loginMerchant = adminSessionManager.getSession(token);
        if(loginMerchant != null){
            return null;
        }

        BasicResult basicResult = new BasicResult();
        basicResult.setSuccess(false);
        basicResult.setCode(BaseCode.TOKEN_ERR);
        basicResult.setMessage("您暂未登录，请登陆后访问");
        log.debug("\n" + JsonUtils.toJson(basicResult));
        /*try {
            response.getWriter().print(JsonUtils.toJson(basicResult));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //token验证失败，不会继续执行后续代码，网关服务直接响应给客户端
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseBody(JsonUtils.toJson(basicResult));
        currentContext.setResponseStatusCode(200);
        return null;
    }
}