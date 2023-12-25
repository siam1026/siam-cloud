package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallOrderRefund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@FeignClient(value = "mall-siam")
public interface PointsMallOrderRefundFeignApi {

    /*@PostMapping(value = "/api/pointsMallOrderRefund/selectSumField")
    BasicResult selectSumField(@RequestBody PointsMallOrderRefund orderRefund);*/

    @PostMapping(value = "/api/pointsMallOrderRefund/selectSumRefundAmount")
    BasicResult<BigDecimal> selectSumRefundAmount(@RequestBody PointsMallOrderRefund orderRefund);

    @PostMapping(value = "/api/pointsMallOrderRefund/selectSumRefundAmountByPlatformCoin")
    BasicResult<BigDecimal> selectSumRefundAmountByPlatformCoin(@RequestBody PointsMallOrderRefund orderRefund);
}