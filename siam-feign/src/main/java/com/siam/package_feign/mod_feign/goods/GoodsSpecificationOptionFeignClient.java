package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.GoodsSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "goods-siam", path = "/rest/goodsSpecificationOptionFeign")
public interface GoodsSpecificationOptionFeignClient {

    @RequestMapping(value = "/selectByPrimaryKey")
    GoodsSpecification selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/selectSumPriceByGoodsIdAndName")
    BigDecimal selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList);
}