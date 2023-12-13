package com.siam.package_promotion.service.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_promotion.model.dto.internal.PointsMallCouponsDto;
import com.siam.package_promotion.entity.internal.PointsMallCoupons;

import java.util.Map;

public interface PointsMallCouponsService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(PointsMallCoupons coupons);

    Map selectCouponsAndGoodsByPrimaryKey(Integer id);

    PointsMallCoupons selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(PointsMallCoupons coupons);

    Page<PointsMallCoupons> getListByPage(int pageNo, int pageSize, PointsMallCoupons coupons);

    Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, PointsMallCoupons coupons);

    Page<Map<String, Object>> getListJoinPointsMallCouponsShopRelationByPage(int pageNo, int pageSize, PointsMallCouponsDto couponsDto);

    void updatePointsMallCouponsEndTime(PointsMallCoupons coupons);

}
