package com.siam.package_user.service.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_user.entity.internal.VipRechargeDenominationCouponsRelation;
import com.siam.package_user.model.example.internal.VipRechargeDenominationCouponsRelationExample;

import java.util.List;

public interface VipRechargeDenominationCouponsRelationService {

    void insertSelective(VipRechargeDenominationCouponsRelation record);

    void insertSelective(Integer vipRechargeDenominationId, List<VipRechargeDenominationCouponsRelation> denominationCouponsRelationList);

    Page<VipRechargeDenominationCouponsRelation> getListByPage(int pageNo, int pageSize, VipRechargeDenominationCouponsRelation vipRechargeDenominationCouponsRelation);

    void deleteByVipRechargeDenominationId(int vipRechargeDenominationId);

    List<VipRechargeDenominationCouponsRelation> selectByExample(VipRechargeDenominationCouponsRelationExample example);

}
