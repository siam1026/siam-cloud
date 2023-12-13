package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.Coupons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "promotion-siam")
public interface CouponsFeignApi {

    @PostMapping(value = "/api/coupons/insertSelective")
    BasicResult insertSelective(@RequestBody Coupons record);

    @PostMapping(value = "/api/coupons/selectByPrimaryKey")
    BasicResult<Coupons> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/coupons/selectCouponsAndGoodsByPrimaryKey")
    BasicResult<Map> selectCouponsAndGoodsByPrimaryKey(@RequestParam("id") Integer id);
}