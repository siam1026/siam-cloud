package com.siam.package_feign.mod_feign.order;

import com.siam.package_order.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order-siam", path = "/rest/orderDetailFeign")
public interface OrderDetailFeignClient {

    @RequestMapping(value = "/selectByOrderId")
    List<OrderDetail> selectByOrderId(@RequestParam("orderId") Integer orderId);
}