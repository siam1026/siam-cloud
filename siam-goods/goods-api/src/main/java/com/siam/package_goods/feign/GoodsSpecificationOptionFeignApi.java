package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_goods.entity.GoodsSpecification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "goods-siam")
public interface GoodsSpecificationOptionFeignApi {

    @PostMapping(value = "/api/goodsSpecificationOption/selectByPrimaryKey")
    BasicResult selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/goodsSpecificationOption/selectSumPriceByGoodsIdAndName")
    BasicResult<BigDecimal> selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList);
}