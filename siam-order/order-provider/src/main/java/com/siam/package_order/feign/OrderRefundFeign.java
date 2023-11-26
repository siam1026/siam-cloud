package com.siam.package_order.feign;

import com.siam.package_common.constant.Quantity;
import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.model.example.OrderRefundExample;
import com.siam.package_order.service.OrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/orderRefundFeign")
public class OrderRefundFeign {

    @Autowired
    private OrderRefundService orderRefundService;

    @RequestMapping(value = "/selectSumField")
    public Map<String, Object> selectSumField(@RequestBody OrderRefund orderRefund){
        return orderRefundService.selectSumField(orderRefund);
    }

    @RequestMapping(value = "/selectSumRefundAmount")
    public BigDecimal selectSumRefundAmount(@RequestBody OrderRefund orderRefund){
        return orderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime());
    }

    @RequestMapping(value = "/selectSumRefundAmountByPlatformCoin")
    public BigDecimal selectSumRefundAmountByPlatformCoin(@RequestBody OrderRefund orderRefund){
        return orderRefundService.selectSumRefundAmount(orderRefund, orderRefund.getStartTime(), orderRefund.getEndTime());
    }

    @RequestMapping(value = "/countByExample")
    public int countByExample(@RequestBody OrderRefund orderRefund){
        OrderRefundExample orderRefundExample = new OrderRefundExample();
        OrderRefundExample.Criteria criteria = orderRefundExample.createCriteria();
        if(orderRefund.getStatus() != null){
            criteria.andStatusEqualTo(Quantity.INT_4);
        }
        return orderRefundService.countByExample(orderRefundExample);
    }
}