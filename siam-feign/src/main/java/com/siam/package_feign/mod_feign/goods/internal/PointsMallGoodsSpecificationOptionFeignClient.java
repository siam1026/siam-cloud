package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.PointsMallGoodsSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/pointsMallGoodsSpecificationOptionFeign")
public interface PointsMallGoodsSpecificationOptionFeignClient {

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallGoodsSpecification selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/selectSumPriceByGoodsIdAndName")
    BigDecimal selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList);
}