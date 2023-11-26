package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.GoodsSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/goodsSpecificationFeign")
public interface GoodsSpecificationFeignClient {

    @RequestMapping(value = "/selectByPrimaryKey")
    GoodsSpecification selectByPrimaryKey(@RequestParam("id") Integer id);

}