package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.CouponsGoodsRelation;
import com.siam.package_promotion.service.CouponsGoodsRelationService;
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
public class CouponsGoodsRelationFeignProvider implements CouponsGoodsRelationFeignApi {

    @Autowired
    private CouponsGoodsRelationService couponsGoodsRelationService;

    public BasicResult insertSelective(CouponsGoodsRelation record){
        couponsGoodsRelationService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult getGoodsIdByCouponsId(@RequestParam("couponsId") Integer couponsId){
        return BasicResult.success(couponsGoodsRelationService.getGoodsIdByCouponsId(couponsId));
    }

    @Override
    public BasicResult deleteByGoodsId(int goodsId) {
        couponsGoodsRelationService.deleteByGoodsId(goodsId);
        return BasicResult.success();
    }
}