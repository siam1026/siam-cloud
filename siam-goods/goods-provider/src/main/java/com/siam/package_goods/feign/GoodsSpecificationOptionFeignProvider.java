package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
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
public class GoodsSpecificationOptionFeignProvider implements GoodsSpecificationOptionFeignApi {

    @Autowired
    private GoodsSpecificationOptionService goodsSpecificationOptionService;

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(goodsSpecificationOptionService.selectByPrimaryKey(id));
    }

    public BasicResult selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList){
        return BasicResult.success(goodsSpecificationOptionService.selectSumPriceByGoodsIdAndName(goodsId, nameList));
    }
}