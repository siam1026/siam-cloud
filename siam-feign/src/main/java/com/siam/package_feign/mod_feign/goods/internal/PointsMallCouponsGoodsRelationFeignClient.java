package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallCouponsGoodsRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/couponsGoodsRelationFeign")
public interface PointsMallCouponsGoodsRelationFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody PointsMallCouponsGoodsRelation record);

    @RequestMapping(value = "/getGoodsIdByCouponsId")
    List<Integer> getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId);
}