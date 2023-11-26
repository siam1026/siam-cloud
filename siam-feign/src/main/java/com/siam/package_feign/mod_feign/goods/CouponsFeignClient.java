package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.Coupons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "goods-siam", path = "/rest/couponsFeign")
public interface CouponsFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Coupons record);

    @RequestMapping(value = "/selectByPrimaryKey")
    Coupons selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/selectCouponsAndGoodsByPrimaryKey")
    Map selectCouponsAndGoodsByPrimaryKey(@RequestParam("id") Integer id);
}