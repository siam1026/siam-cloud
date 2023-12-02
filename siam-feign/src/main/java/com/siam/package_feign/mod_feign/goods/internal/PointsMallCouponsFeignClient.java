package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallCoupons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "goods-siam", path = "/rest/couponsFeign")
public interface PointsMallCouponsFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallCoupons record);

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallCoupons selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/selectCouponsAndGoodsByPrimaryKey")
    Map selectCouponsAndGoodsByPrimaryKey(@RequestParam("id") Integer id);
}