package com.siam.package_user.service_impl.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_user.mapper.internal.VipRechargeDenominationMapper;
import com.siam.package_user.service.internal.VipRechargeDenominationService;
import com.siam.package_user.entity.internal.VipRechargeDenomination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VipRechargeDenominationServiceImpl implements VipRechargeDenominationService {

    @Autowired
    private VipRechargeDenominationMapper vipRechargeDenominationMapper;

    @Override
    public void deleteByPrimaryKey(Integer id) {
        vipRechargeDenominationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insertSelective(VipRechargeDenomination vipRechargeDenomination) {
        vipRechargeDenominationMapper.insertSelective(vipRechargeDenomination);
    }

    @Override
    public VipRechargeDenomination selectByPrimaryKey(Integer id) {
        return vipRechargeDenominationMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(VipRechargeDenomination vipRechargeDenomination) {
        vipRechargeDenominationMapper.updateByPrimaryKeySelective(vipRechargeDenomination);
    }

    @Override
    public Page getListByPage(int pageNo, int pageSize, VipRechargeDenomination vipRechargeDenomination) {
        Page<Map<String, Object>> page = vipRechargeDenominationMapper.getListByPage(new Page(pageNo, pageSize), vipRechargeDenomination);
        return page;
    }
}