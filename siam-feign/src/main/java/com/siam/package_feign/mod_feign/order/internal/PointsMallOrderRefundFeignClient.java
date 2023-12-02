package com.siam.package_feign.mod_feign.order.internal;

import com.siam.package_order.entity.internal.PointsMallOrderRefund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "order-siam", path = "/rest/pointsMallOrderRefundFeign")
public interface PointsMallOrderRefundFeignClient {

    @RequestMapping(value = "/selectSumField")
    Map<String, Object> selectSumField(@RequestBody PointsMallOrderRefund orderRefund);

    @RequestMapping(value = "/selectSumRefundAmount")
    BigDecimal selectSumRefundAmount(@RequestBody PointsMallOrderRefund orderRefund);

    @RequestMapping(value = "/selectSumRefundAmountByPlatformCoin")
    BigDecimal selectSumRefundAmountByPlatformCoin(@RequestBody PointsMallOrderRefund orderRefund);
}