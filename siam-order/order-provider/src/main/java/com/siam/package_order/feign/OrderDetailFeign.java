package com.siam.package_order.feign;

import com.siam.package_order.entity.OrderDetail;
import com.siam.package_order.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/orderDetailFeign")
public class OrderDetailFeign {

    @Autowired
    private OrderDetailService orderDetailService;


    @RequestMapping(value = "/selectByOrderId")
    public List<OrderDetail> selectByOrderId(@RequestParam("orderId") Integer orderId){
        return orderDetailService.selectByOrderId(orderId);
    }
}