package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.SysMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "goods-siam", path = "/rest/messageFeign")
public interface MessageFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody SysMessage record);
}