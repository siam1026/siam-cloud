package com.siam.package_promotion.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.internal.PointsMallCoupons;
import com.siam.package_promotion.service.internal.PointsMallCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallCouponsFeignProvider implements PointsMallCouponsFeignApi {

    @Autowired
    private PointsMallCouponsService pointsMallCouponsService;

    @Override
    public BasicResult insertSelective(PointsMallCoupons record) {
        pointsMallCouponsService.insertSelective(record);
        return BasicResult.success();
    }

    @Override
    public BasicResult<PointsMallCoupons> selectByPrimaryKey(Integer id) {
        return BasicResult.success(pointsMallCouponsService.selectByPrimaryKey(id));
    }

    @Override
    public BasicResult<Map> selectCouponsAndGoodsByPrimaryKey(Integer id) {
        return BasicResult.success(pointsMallCouponsService.selectCouponsAndGoodsByPrimaryKey(id));
    }
}