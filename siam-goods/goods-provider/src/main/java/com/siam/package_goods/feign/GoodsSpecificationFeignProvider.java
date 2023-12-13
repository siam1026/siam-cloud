package com.siam.package_goods.feign;

import com.siam.package_common.entity.BasicResult;
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
public class GoodsSpecificationFeignProvider implements GoodsSpecificationFeignApi {

    @Autowired
    private GoodsSpecificationService goodsSpecificationService;

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(goodsSpecificationService.selectByPrimaryKey(id));
    }
}