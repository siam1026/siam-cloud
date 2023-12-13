package com.siam.package_user.service.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_user.entity.internal.VipRechargeDenomination;

/**
 *  暹罗
 */
public interface VipRechargeDenominationService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(VipRechargeDenomination vipRechargeDenomination);

    VipRechargeDenomination selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(VipRechargeDenomination vipRechargeDenomination);

    Page getListByPage(int pageNo, int pageSize, VipRechargeDenomination vipRechargeDenomination);

}
