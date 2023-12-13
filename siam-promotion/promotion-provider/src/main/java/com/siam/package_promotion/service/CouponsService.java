package com.siam.package_promotion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_promotion.model.dto.CouponsDto;
import com.siam.package_promotion.entity.Coupons;

import java.util.Map;

public interface CouponsService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(Coupons coupons);

    Map selectCouponsAndGoodsByPrimaryKey(Integer id);

    Coupons selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Coupons coupons);

    Page<Coupons> getListByPage(int pageNo, int pageSize, Coupons coupons);

    Page<Map<String, Object>> getMapListByPage(int pageNo, int pageSize, Coupons coupons);

    Page<Map<String, Object>> getListJoinCouponsShopRelationByPage(int pageNo, int pageSize, CouponsDto couponsDto);

    void updateCouponsEndTime(Coupons coupons);

}
