package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallGoodsSpecificationOption;
import com.siam.package_mall.service.PointsMallGoodsSpecificationOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallGoodsSpecificationOptionFeignProvider implements PointsMallGoodsSpecificationOptionFeignApi {

    @Autowired
    private PointsMallGoodsSpecificationOptionService pointsMallGoodsSpecificationOptionService;

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(pointsMallGoodsSpecificationOptionService.selectByPrimaryKey(id));
    }

    public BasicResult selectSumPriceByGoodsIdAndName(@RequestParam("goodsId") Integer goodsId, @RequestParam("nameList") List<String> nameList){
        return BasicResult.success(pointsMallGoodsSpecificationOptionService.selectSumPriceByGoodsIdAndName(goodsId, nameList));
    }
}