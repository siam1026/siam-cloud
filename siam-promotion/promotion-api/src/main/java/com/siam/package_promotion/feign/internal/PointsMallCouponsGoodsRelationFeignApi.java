package com.siam.package_promotion.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.internal.PointsMallCouponsGoodsRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "promotion-siam")
public interface PointsMallCouponsGoodsRelationFeignApi {

    @PostMapping(value = "/api/pointsMallCouponsGoodsRelation/insertSelective")
    BasicResult insertSelective(@RequestBody PointsMallCouponsGoodsRelation record);

    @PostMapping(value = "/api/pointsMallCouponsGoodsRelation/getGoodsIdByCouponsId")
    BasicResult<List<Integer>> getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId);

    @PostMapping(value = "/api/pointsMallCouponsGoodsRelation/deleteByPointsMallGoodsId")
    BasicResult deleteByPointsMallGoodsId(@RequestParam("goodsId") int goodsId);
}