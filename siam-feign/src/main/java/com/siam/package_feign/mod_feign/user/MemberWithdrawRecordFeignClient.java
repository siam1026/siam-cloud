package com.siam.package_feign.mod_feign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "user-siam", path = "/rest/memberWithdrawRecordFeign")
public interface MemberWithdrawRecordFeignClient {

    @RequestMapping(value = "/autoPayment")
    void autoPayment();
}