package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "user-siam")
public interface MemberWithdrawRecordFeignApi {

    @PostMapping(value = "/api/memberWithdrawRecord/autoPayment")
    BasicResult autoPayment();
}