package com.siam.package_mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.entity.PointsMallCoupons;
import com.siam.package_mall.model.dto.PointsMallCouponsDto;

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
