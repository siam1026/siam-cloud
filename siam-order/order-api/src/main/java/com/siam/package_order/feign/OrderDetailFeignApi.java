package com.siam.package_order.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "order-siam")
public interface OrderDetailFeignApi {

    @PostMapping(value = "/api/orderDetail/selectByOrderId")
    BasicResult selectByOrderId(@RequestParam("orderId") Integer orderId);
}