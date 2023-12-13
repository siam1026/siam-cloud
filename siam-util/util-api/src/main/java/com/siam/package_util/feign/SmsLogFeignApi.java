package com.siam.package_util.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_util.entity.SmsLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "util-siam")
public interface SmsLogFeignApi {

    @PostMapping(value = "/api/smsLog/getLastLog")
    BasicResult<SmsLog> getLastLog(@RequestParam("mobile") String mobile, @RequestParam("type") String type, @RequestParam("minutes") int minutes);
}