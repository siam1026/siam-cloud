package com.siam.package_feign.mod_feign.order;

import com.siam.package_weixin_pay.entity.TransfersDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "order-siam", path = "/rest/WxPayFeign")
public interface WxPayFeignClient {

    /**
     * 企业付款到零钱
     */
    @RequestMapping(value = "/payToBalance")
    boolean payToBalance(@RequestBody TransfersDto model);
}