package com.siam.package_feign.mod_feign.user;

import com.siam.package_common.entity.BasicResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "user-siam", path = "/rest/merchantWithdrawRecordFeign")
public interface MerchantWithdrawRecordFeignClient {

    @RequestMapping(value = "/countByAuditStatus")
    BasicResult countByAuditStatus(@RequestBody int auditStatus);

}