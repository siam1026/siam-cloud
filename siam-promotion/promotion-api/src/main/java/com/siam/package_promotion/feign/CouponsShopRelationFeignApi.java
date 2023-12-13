package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsShopRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "promotion-siam")
public interface CouponsShopRelationFeignApi {

    @PostMapping(value = "/api/couponsShopRelation/insertSelective")
    BasicResult insertSelective(@RequestBody CouponsShopRelation record);

    @PostMapping(value = "/api/couponsShopRelation/getShopIdByCouponsId")
    BasicResult<List<Integer>> getShopIdByCouponsId(@RequestParam("id") Integer couponsId);
}