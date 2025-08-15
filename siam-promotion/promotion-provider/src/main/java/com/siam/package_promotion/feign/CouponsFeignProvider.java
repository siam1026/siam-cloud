package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.Coupons;
import com.siam.package_promotion.service.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class CouponsFeignProvider implements CouponsFeignApi {

    @Autowired
    private CouponsService couponsService;

    @Override
    public BasicResult insertSelective(Coupons record) {
        couponsService.insertSelective(record);
        return BasicResult.success();
    }

    @Override
    public BasicResult<Coupons> selectByPrimaryKey(Integer id) {
        return BasicResult.success(couponsService.selectByPrimaryKey(id));
    }

    @Override
    public BasicResult<Map> selectCouponsAndGoodsByPrimaryKey(Integer id) {
        return BasicResult.success(couponsService.selectCouponsAndGoodsByPrimaryKey(id));
    }
}