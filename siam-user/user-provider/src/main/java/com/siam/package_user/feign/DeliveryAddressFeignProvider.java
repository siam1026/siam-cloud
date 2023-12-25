package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class DeliveryAddressFeignProvider implements DeliveryAddressFeignApi {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(deliveryAddressService.selectByPrimaryKey(id));
    }
}