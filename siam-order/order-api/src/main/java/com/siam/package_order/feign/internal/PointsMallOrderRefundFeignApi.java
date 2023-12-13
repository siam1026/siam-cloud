package com.siam.package_order.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.internal.PointsMallOrderRefund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "order-siam")
public interface PointsMallOrderRefundFeignApi {

    /*@PostMapping(value = "/api/pointsMallOrderRefund/selectSumField")
    BasicResult selectSumField(@RequestBody PointsMallOrderRefund orderRefund);*/

    @PostMapping(value = "/api/pointsMallOrderRefund/selectSumRefundAmount")
    BasicResult<BigDecimal> selectSumRefundAmount(@RequestBody PointsMallOrderRefund orderRefund);

    @PostMapping(value = "/api/pointsMallOrderRefund/selectSumRefundAmountByPlatformCoin")
    BasicResult<BigDecimal> selectSumRefundAmountByPlatformCoin(@RequestBody PointsMallOrderRefund orderRefund);
}