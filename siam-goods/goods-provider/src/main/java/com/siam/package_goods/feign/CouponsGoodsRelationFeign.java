package com.siam.package_goods.feign;

import com.siam.package_goods.entity.CouponsGoodsRelation;
import com.siam.package_goods.service.CouponsGoodsRelationService;
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
@RequestMapping(value = "/rest/couponsGoodsRelationFeign")
public class CouponsGoodsRelationFeign {

    @Autowired
    private CouponsGoodsRelationService couponsGoodsRelationService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody CouponsGoodsRelation record){
        couponsGoodsRelationService.insertSelective(record);
    }

    @RequestMapping(value = "/getGoodsIdByCouponsId")
    public List<Integer> getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId){
        return couponsGoodsRelationService.getGoodsIdByCouponsId(couponsId);
    }
}