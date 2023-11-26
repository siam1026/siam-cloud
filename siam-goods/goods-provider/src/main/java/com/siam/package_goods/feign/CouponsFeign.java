package com.siam.package_goods.feign;

import com.siam.package_goods.entity.Coupons;
import com.siam.package_goods.service.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/couponsFeign")
public class CouponsFeign {

    @Autowired
    private CouponsService couponsService;

    @RequestMapping(value = "/insertSelective")
    public void insertSelective(@RequestBody Coupons record){
        couponsService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    public Coupons selectByPrimaryKey(@RequestParam("id") Integer id){
        return couponsService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/selectCouponsAndGoodsByPrimaryKey")
    public Map selectCouponsAndGoodsByPrimaryKey(@RequestParam("id") Integer id){
        return couponsService.selectCouponsAndGoodsByPrimaryKey(id);
    }
}