package com.siam.package_promotion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.package_promotion.entity.Advertisement;
import com.siam.package_promotion.model.param.AdvertisementParam;

/**
 *  暹罗
 */
public interface AdvertisementService extends IService<Advertisement> {

    void deleteByPrimaryKey(AdvertisementParam param);

    void insertSelective(AdvertisementParam param);

    Advertisement selectByPrimaryKey(AdvertisementParam param);

    void updateByPrimaryKeySelective(AdvertisementParam param);

    Page getListByPage(AdvertisementParam param);

}