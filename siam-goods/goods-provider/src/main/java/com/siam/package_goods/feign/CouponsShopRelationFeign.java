package com.siam.package_goods.feign;

import com.siam.package_goods.entity.CouponsShopRelation;
import com.siam.package_goods.service.CouponsShopRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/couponsShopRelationFeign")
public class CouponsShopRelationFeign {

    @Autowired
    private CouponsShopRelationService couponsShopRelationService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody CouponsShopRelation record){
        couponsShopRelationService.insertSelective(record);
    }

    @RequestMapping(value = "/getShopIdByCouponsId")
    List<Integer> getShopIdByCouponsId(@RequestParam("id") Integer couponsId){
        return couponsShopRelationService.getShopIdByCouponsId(couponsId);
    }
}