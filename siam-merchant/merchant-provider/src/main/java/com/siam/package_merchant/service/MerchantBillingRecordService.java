package com.siam.package_merchant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_merchant.entity.MerchantBillingRecord;
import com.siam.package_merchant.model.param.MerchantBillingRecordParam;

import java.util.Map;

/**
 *  暹罗
 */
public interface MerchantBillingRecordService {

    void deleteByPrimaryKey(Integer id);

    void insertSelective(MerchantBillingRecord merchantBillingRecord);

    MerchantBillingRecord selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(MerchantBillingRecord merchantBillingRecord);

    Page getListByPage(MerchantBillingRecordParam param);

    Page getListByPageJoinShop(MerchantBillingRecordParam param);

    Map<String, Object> statisticalAmount(MerchantBillingRecordParam param);
}