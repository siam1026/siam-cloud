package com.siam.package_goods.feign;

import com.siam.package_goods.entity.GoodsSpecification;
import com.siam.package_goods.service.GoodsSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户Token模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/goodsSpecificationFeign")
public class GoodsSpecificationFeign {

    @Autowired
    private GoodsSpecificationService goodsSpecificationService;

    @RequestMapping(value = "/selectByPrimaryKey")
    public GoodsSpecification selectByPrimaryKey(@RequestParam("id") Integer id){
        return goodsSpecificationService.selectByPrimaryKey(id);
    }
}