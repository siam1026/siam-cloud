package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.CouponsGoodsRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/couponsGoodsRelationFeign")
public interface CouponsGoodsRelationFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody CouponsGoodsRelation record);

    @RequestMapping(value = "/getGoodsIdByCouponsId")
    List<Integer> getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId);
}