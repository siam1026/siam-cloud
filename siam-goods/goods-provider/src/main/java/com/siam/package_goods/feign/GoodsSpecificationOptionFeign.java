package com.siam.package_goods.feign;

import com.siam.package_goods.entity.GoodsSpecificationOption;
import com.siam.package_goods.service.GoodsSpecificationOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/goodsSpecificationOptionFeign")
public class GoodsSpecificationOptionFeign {

    @Autowired
    private GoodsSpecificationOptionService goodsSpecificationOptionService;

    @RequestMapping(value = "/selectByPrimaryKey")
    GoodsSpecificationOption selectByPrimaryKey(@RequestParam("id") Integer id){
        return goodsSpecificationOptionService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/selectSumPriceByGoodsIdAndName")
    BigDecimal selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList){
        return goodsSpecificationOptionService.selectSumPriceByGoodsIdAndName(goodsId, nameList);
    }
}