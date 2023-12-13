package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.GoodsSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam")
public interface GoodsSpecificationFeignApi {

    @PostMapping(value = "/api/goodsSpecification/selectByPrimaryKey")
    BasicResult selectByPrimaryKey(@RequestParam("id") Integer id);

}