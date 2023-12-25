package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.DeliveryAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-siam")
public interface DeliveryAddressFeignApi {

    @PostMapping(value = "/api/deliveryAddress/selectByPrimaryKey")
    BasicResult<DeliveryAddress> selectByPrimaryKey(@RequestParam("id") Integer id);
}