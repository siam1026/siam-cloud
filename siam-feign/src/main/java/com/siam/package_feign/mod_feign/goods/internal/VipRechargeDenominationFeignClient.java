package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.VipRechargeDenomination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/vipRechargeRecordFeign")
public interface VipRechargeDenominationFeignClient {

    @RequestMapping(value = "/selectLastestPaid")
    VipRechargeDenomination selectByPrimaryKey(@RequestParam("memberId") Integer id);
}