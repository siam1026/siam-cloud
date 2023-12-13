package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsShopRelation;
import com.siam.package_promotion.service.CouponsShopRelationService;
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
public class CouponsShopRelationFeignProvider implements CouponsShopRelationFeignApi {

    @Autowired
    private CouponsShopRelationService couponsShopRelationService;

    public BasicResult insertSelective(CouponsShopRelation record){
        couponsShopRelationService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult getShopIdByCouponsId(@RequestParam("id") Integer couponsId){
        return BasicResult.success(couponsShopRelationService.getShopIdByCouponsId(couponsId));
    }
}