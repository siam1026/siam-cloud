package com.siam.package_zuul.config;

import com.siam.package_common.filter.StoneFilterRegistration;
import com.siam.package_common.filter.StoneFilterRegistry;
import com.siam.package_common.filter.StoneMappedFilter;
import com.siam.package_zuul.filter.AdminFilter;
import com.siam.package_zuul.filter.EncryptFilter;
import com.siam.package_zuul.filter.MemberFilter;
import com.siam.package_zuul.filter.MerchantFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;

@Configuration
public class WebMvcConfig {

    @Autowired
    private MemberFilter memberFilter;

    @Autowired
    private MerchantFilter merchantFilter;

    @Autowired
    private AdminFilter adminFilter;

    @Autowired
    private EncryptFilter encryptFilter;

    @Autowired
    private StoneFilterRegistry registry;

    @PostConstruct
    public void init(){
        addFilters();
    }

    /**
     * 重写添加拦截器方法并添加配置拦截器
     **/
    public void addFilters() {
        //用户拦截器
        registry.addFilter(memberFilter)
//                .addPathPatterns("/api-member/rest/member/**")
                .addPathPatterns("/*/rest/member/**")
                .excludePathPatterns("/api-user/rest/member/login")
                .excludePathPatterns("/api-user/rest/member/loginByMobile")
//                .excludePathPatterns("/api-user/rest/member/logout")
                .excludePathPatterns("/api-user/rest/member/register")
                .excludePathPatterns("/api-user/rest/member/verification/login")
                .excludePathPatterns("/api-user/rest/member/weChat/login")
                .excludePathPatterns("/api-order/rest/member/wxPay/notify")
                .excludePathPatterns("/api-order/rest/member/wxPay/refundSuccessNotify");;

        //商户拦截器
        registry.addFilter(merchantFilter)
//                .addPathPatterns("/api-member/rest/member/**")
                .addPathPatterns("/*/rest/merchant/**")
                .excludePathPatterns("/api-merchant/rest/merchant/login")
                .excludePathPatterns("/api-merchant/rest/merchant/loginByMobile")
                .excludePathPatterns("/api-merchant/rest/merchant/logout")
                .excludePathPatterns("/api-merchant/rest/merchant/register")
                .excludePathPatterns("/api-merchant/rest/merchant/registerByMobile")
                .excludePathPatterns("/api-merchant/rest/merchant/verification/login")
                .excludePathPatterns("/api-merchant/rest/merchant/forgetPassword/step1");

        //管理员拦截器
        registry.addFilter(adminFilter)
//                .addPathPatterns("/api-admin/rest/admin/**")
                .addPathPatterns("/*/rest/admin/**")
                .excludePathPatterns("/api-user/rest/admin/login")
                .excludePathPatterns("/api-user/rest/admin/login")
                .excludePathPatterns("/api-user/rest/admin/loginByMobile")
                .excludePathPatterns("/api-user/rest/admin/logout")
                .excludePathPatterns("/api-user/rest/admin/register")
                .excludePathPatterns("/api-user/rest/admin/verification/login")
                .excludePathPatterns("/api-user/rest/admin/forgetPassword/step1");
    }

    public boolean matches(String lookupPath) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        /*System.out.println(stackTraceElements.length);
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            System.out.println(stackTraceElement.getClassName());
        }*/
        StoneFilterRegistration registration = registry.getFilter(stackTraceElements[2].getClassName());
        if(registration == null){
            return false;
        }else{
            StoneMappedFilter filter = (StoneMappedFilter) registration.getFilter();
            return filter.matches(lookupPath, new AntPathMatcher());
        }
    }
}