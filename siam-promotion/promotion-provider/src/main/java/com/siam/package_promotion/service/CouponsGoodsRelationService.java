package com.siam.package_promotion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_promotion.entity.CouponsGoodsRelation;

import java.util.List;

public interface CouponsGoodsRelationService {

    void insertSelective(CouponsGoodsRelation record);

    void insertSelective(Integer couponsId, List<Integer> goodsIdList);

    Page<CouponsGoodsRelation> getListByPage(int pageNo, int pageSize, CouponsGoodsRelation couponsGoodsRelation);

    List<Integer> getGoodsIdByCouponsId(Integer couponsId);

    void deleteByGoodsId(int goodsId);

}
