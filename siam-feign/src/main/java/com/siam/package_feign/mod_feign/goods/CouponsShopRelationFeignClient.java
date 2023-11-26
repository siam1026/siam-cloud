package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.CouponsShopRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/couponsShopRelationFeign")
public interface CouponsShopRelationFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody CouponsShopRelation record);

    @RequestMapping(value = "/getShopIdByCouponsId")
    List<Integer> getShopIdByCouponsId(@RequestParam("id") Integer couponsId);
}