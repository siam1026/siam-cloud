package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallCouponsGoodsRelation;
import com.siam.package_mall.service.PointsMallCouponsGoodsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallCouponsGoodsRelationFeignProvider implements PointsMallCouponsGoodsRelationFeignApi {

    @Autowired
    private PointsMallCouponsGoodsRelationService pointsMallCouponsGoodsRelationService;

    @Override
    public BasicResult insertSelective(PointsMallCouponsGoodsRelation record) {
        pointsMallCouponsGoodsRelationService.insertSelective(record);
        return BasicResult.success();
    }

    @Override
    public BasicResult<List<Integer>> getGoodsIdByCouponsId(Integer couponsId) {
        return BasicResult.success(pointsMallCouponsGoodsRelationService.getGoodsIdByCouponsId(couponsId));
    }

    @Override
    public BasicResult deleteByPointsMallGoodsId(int goodsId) {
        pointsMallCouponsGoodsRelationService.deleteByPointsMallGoodsId(goodsId);
        return BasicResult.success();
    }
}