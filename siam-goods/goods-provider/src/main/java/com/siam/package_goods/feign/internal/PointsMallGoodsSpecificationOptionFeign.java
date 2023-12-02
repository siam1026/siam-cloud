package com.siam.package_goods.feign.internal;

import com.siam.package_goods.entity.internal.PointsMallGoodsSpecificationOption;
import com.siam.package_goods.service.internal.PointsMallGoodsSpecificationOptionService;
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
@RequestMapping(value = "/rest/pointsMallGoodsSpecificationOptionFeign")
public class PointsMallGoodsSpecificationOptionFeign {

    @Autowired
    private PointsMallGoodsSpecificationOptionService pointsMallGoodsSpecificationOptionService;

    @RequestMapping(value = "/selectByPrimaryKey")
    PointsMallGoodsSpecificationOption selectByPrimaryKey(@RequestParam("id") Integer id){
        return pointsMallGoodsSpecificationOptionService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/selectSumPriceByGoodsIdAndName")
    BigDecimal selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList){
        return pointsMallGoodsSpecificationOptionService.selectSumPriceByGoodsIdAndName(goodsId, nameList);
    }
}