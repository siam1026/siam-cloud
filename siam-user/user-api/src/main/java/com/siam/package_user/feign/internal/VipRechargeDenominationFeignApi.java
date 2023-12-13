package com.siam.package_user.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.internal.VipRechargeDenomination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-siam")
public interface VipRechargeDenominationFeignApi {

    @PostMapping(value = "/api/vipRechargeDenomination/selectLastestPaid")
    BasicResult<VipRechargeDenomination> selectByPrimaryKey(@RequestParam("memberId") Integer id);
}