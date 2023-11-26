package com.siam.package_feign.mod_feign.order;

import com.siam.package_order.entity.OrderRefund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "order-siam", path = "/rest/orderRefundFeign")
public interface OrderRefundFeignClient {

    @RequestMapping(value = "/selectSumField")
    Map<String, Object> selectSumField(@RequestBody OrderRefund orderRefund);

    @RequestMapping(value = "/selectSumRefundAmount")
    BigDecimal selectSumRefundAmount(@RequestBody OrderRefund orderRefund);

    @RequestMapping(value = "/selectSumRefundAmountByPlatformCoin")
    BigDecimal selectSumRefundAmountByPlatformCoin(@RequestBody OrderRefund orderRefund);

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody OrderRefund example);
}