package com.siam.package_merchant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_merchant.entity.ShopChangeRecord;
import com.siam.package_merchant.model.example.ShopChangeRecordExample;

/**
 *  暹罗
 */
public interface ShopChangeRecordService {

    int countByExample(ShopChangeRecordExample example);

    void deleteByPrimaryKey(Integer id);

    void insertSelective(ShopChangeRecord shopChangeRecord);

    ShopChangeRecord selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ShopChangeRecord shopChangeRecord);

    Page getListByPage(int pageNo, int pageSize, ShopChangeRecord shopChangeRecord);

    Page getListByPageJoinShop(int pageNo, int pageSize, ShopChangeRecord shopChangeRecord);

    ShopChangeRecord selectLastestByShopId(Integer shopId);
}
