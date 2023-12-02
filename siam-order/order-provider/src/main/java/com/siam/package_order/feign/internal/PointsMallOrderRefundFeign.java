package com.siam.package_order.feign.internal;

import com.siam.package_order.entity.internal.PointsMallOrderRefund;
import com.siam.package_order.service.internal.PointsMallOrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/pointsMallOrderRefundFeign")
public class PointsMallOrderRefundFeign {

    @Autowired
    private PointsMallOrderRefundService pointsMallOrderRefundService;

//    @RequestMapping(value = "/selectSumField")
//    public Map<String, Object> selectSumField(@RequestBody PointsMallOrderRefund orderRefund){
//        return pointsMallOrderRefundService.selectSumField(orderRefund);
//    }

    @RequestMapping(value = "/selectSumRefundAmount")
    public BigDecimal selectSumRefundAmount(@RequestBody PointsMallOrderRefund orderRefund){
        return pointsMallOrderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime());
    }

    @RequestMapping(value = "/selectSumRefundAmountByPlatformCoin")
    public BigDecimal selectSumRefundAmountByPlatformCoin(@RequestBody PointsMallOrderRefund orderRefund){
        return pointsMallOrderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime());
    }
}