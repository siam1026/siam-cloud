package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "merchant-siam")
public interface MerchantWithdrawRecordFeignApi {

    @PostMapping(value = "/api/merchantWithdrawRecord/countByAuditStatus")
    BasicResult<Integer> countByAuditStatus(@RequestParam("auditStatus") int auditStatus);

}