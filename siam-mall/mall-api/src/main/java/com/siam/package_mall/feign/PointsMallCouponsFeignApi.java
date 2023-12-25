package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallCoupons;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "mall-siam")
public interface PointsMallCouponsFeignApi {

    @PostMapping(value = "/api/pointsMallCoupons/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallCoupons record);

    @PostMapping(value = "/api/pointsMallCoupons/selectByPrimaryKey")
    BasicResult<PointsMallCoupons> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/pointsMallCoupons/selectCouponsAndGoodsByPrimaryKey")
    BasicResult<Map> selectCouponsAndGoodsByPrimaryKey(@RequestParam("id") Integer id);
}