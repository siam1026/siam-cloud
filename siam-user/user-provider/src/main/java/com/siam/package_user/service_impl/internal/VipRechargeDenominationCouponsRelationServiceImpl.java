package com.siam.package_user.service_impl.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_user.entity.internal.VipRechargeDenominationCouponsRelation;
import com.siam.package_user.mapper.internal.VipRechargeDenominationCouponsRelationMapper;
import com.siam.package_user.model.example.internal.VipRechargeDenominationCouponsRelationExample;
import com.siam.package_user.service.internal.VipRechargeDenominationCouponsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VipRechargeDenominationCouponsRelationServiceImpl implements VipRechargeDenominationCouponsRelationService {

    @Autowired
    private VipRechargeDenominationCouponsRelationMapper vipRechargeDenominationCouponsRelationMapper;

    @Override
    public void insertSelective(VipRechargeDenominationCouponsRelation record) {
        record.setCreateTime(new Date());
        vipRechargeDenominationCouponsRelationMapper.insertSelective(record);
    }

    @Override
    public void insertSelective(Integer vipRechargeDenominationId, List<VipRechargeDenominationCouponsRelation> denominationCouponsRelationList) {
        //1、删除所有关系
        vipRechargeDenominationCouponsRelationMapper.deleteByVipRechargeDenominationId(vipRechargeDenominationId);

        //2、创建关系
        for (VipRechargeDenominationCouponsRelation denominationCouponsRelation : denominationCouponsRelationList) {
            VipRechargeDenominationCouponsRelation vipRechargeDenominationCouponsRelation = new VipRechargeDenominationCouponsRelation();
            vipRechargeDenominationCouponsRelation.setVipRechargeDenominationId(vipRechargeDenominationId);
            vipRechargeDenominationCouponsRelation.setCouponsId(denominationCouponsRelation.getCouponsId());
            vipRechargeDenominationCouponsRelation.setGiveQuantity(denominationCouponsRelation.getGiveQuantity());
            vipRechargeDenominationCouponsRelation.setCreateTime(new Date());
            vipRechargeDenominationCouponsRelationMapper.insertSelective(vipRechargeDenominationCouponsRelation);
        }
    }

    @Override
    public Page<VipRechargeDenominationCouponsRelation> getListByPage(int pageNo, int pageSize, VipRechargeDenominationCouponsRelation vipRechargeDenominationCouponsRelation) {
        Page<VipRechargeDenominationCouponsRelation> page = vipRechargeDenominationCouponsRelationMapper.getListByPage(new Page(pageNo, pageSize), vipRechargeDenominationCouponsRelation);

        return page;
    }

    @Override
    public void deleteByVipRechargeDenominationId(int vipRechargeDenominationId) {
        vipRechargeDenominationCouponsRelationMapper.deleteByVipRechargeDenominationId(vipRechargeDenominationId);
    }

    @Override
    public List<VipRechargeDenominationCouponsRelation> selectByExample(VipRechargeDenominationCouponsRelationExample example) {
        return vipRechargeDenominationCouponsRelationMapper.selectByExample(example);
    }
}
