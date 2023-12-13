package com.siam.package_order.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.OrderRefund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "order-siam")
public interface OrderRefundFeignApi {

    @PostMapping(value = "/api/orderRefund/selectSumField")
    BasicResult<Map<String, Object>> selectSumField(@RequestBody OrderRefund orderRefund);

    @PostMapping(value = "/api/orderRefund/selectSumRefundAmount")
    BasicResult<BigDecimal> selectSumRefundAmount(@RequestBody OrderRefund orderRefund);

    @PostMapping(value = "/api/orderRefund/selectSumRefundAmountByPlatformCoin")
    BasicResult<BigDecimal> selectSumRefundAmountByPlatformCoin(@RequestBody OrderRefund orderRefund);

    @PostMapping(value = "/api/orderRefund/countByExample")
    BasicResult<Integer> countByExample(@RequestBody OrderRefund example);
}