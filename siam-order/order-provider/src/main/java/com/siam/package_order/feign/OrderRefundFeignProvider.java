package com.siam.package_order.feign;

import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.model.example.OrderRefundExample;
import com.siam.package_order.service.OrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class OrderRefundFeignProvider implements OrderRefundFeignApi {

    @Autowired
    private OrderRefundService orderRefundService;

    public BasicResult selectSumField(OrderRefund orderRefund){
        return BasicResult.success(orderRefundService.selectSumField(orderRefund));
    }

    public BasicResult selectSumRefundAmount(OrderRefund orderRefund){
        return BasicResult.success(orderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime()));
    }

    public BasicResult selectSumRefundAmountByPlatformCoin(OrderRefund orderRefund){
        return BasicResult.success(orderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime()));
    }

    public BasicResult countByExample(OrderRefund orderRefund){
        OrderRefundExample orderRefundExample = new OrderRefundExample();
        OrderRefundExample.Criteria criteria = orderRefundExample.createCriteria();
        if(orderRefund.getStatus() != null){
            criteria.andStatusEqualTo(Quantity.INT_4);
        }
        return BasicResult.success(orderRefundService.countByExample(orderRefundExample));
    }
}