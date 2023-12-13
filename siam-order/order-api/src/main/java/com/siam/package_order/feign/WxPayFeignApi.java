package com.siam.package_order.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_weixin_pay.entity.TransfersDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "order-siam")
public interface WxPayFeignApi {

    /**
     * 企业付款到零钱
     */
    @PostMapping(value = "/api/WxPay/payToBalance")
    BasicResult<Boolean> payToBalance(@RequestBody TransfersDto model);
}