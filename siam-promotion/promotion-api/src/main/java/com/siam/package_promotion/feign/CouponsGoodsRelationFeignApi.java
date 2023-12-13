package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsGoodsRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "promotion-siam")
public interface CouponsGoodsRelationFeignApi {

    @PostMapping(value = "/api/couponsGoodsRelation/insertSelective")
    BasicResult insertSelective(@RequestBody CouponsGoodsRelation record);

    @PostMapping(value = "/api/couponsGoodsRelation/getGoodsIdByCouponsId")
    BasicResult<List<Integer>> getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId);

    @PostMapping(value = "/api/couponsGoodsRelation/deleteByGoodsId")
    BasicResult deleteByGoodsId(@RequestParam("goodsId") int goodsId);
}