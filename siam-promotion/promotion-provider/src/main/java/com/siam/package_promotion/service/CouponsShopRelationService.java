package com.siam.package_promotion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_promotion.entity.CouponsShopRelation;

import java.util.List;

public interface CouponsShopRelationService {

    void insertSelective(CouponsShopRelation record);

    void insertSelective(Integer couponsId, List<Integer> shopIdList);

    Page<CouponsShopRelation> getListByPage(int pageNo, int pageSize, CouponsShopRelation couponsShopRelation);

    void deleteByShopId(int shopId);

    List<Integer> getShopIdByCouponsId(Integer couponsId);
}