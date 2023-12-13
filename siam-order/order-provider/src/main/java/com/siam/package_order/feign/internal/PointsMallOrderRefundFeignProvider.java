package com.siam.package_order.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.internal.PointsMallOrderRefund;
import com.siam.package_order.service.internal.PointsMallOrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallOrderRefundFeignProvider implements PointsMallOrderRefundFeignApi {

    @Autowired
    private PointsMallOrderRefundService pointsMallOrderRefundService;

    /*public BasicResult selectSumField(PointsMallOrderRefund orderRefund){
        return BasicResult.success(pointsMallOrderRefundService.selectSumField(orderRefund));
    }*/

    public BasicResult selectSumRefundAmount(PointsMallOrderRefund orderRefund){
        return BasicResult.success(pointsMallOrderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime()));
    }

    public BasicResult selectSumRefundAmountByPlatformCoin(PointsMallOrderRefund orderRefund){
        return BasicResult.success(pointsMallOrderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime()));
    }
}