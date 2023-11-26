package com.siam.package_feign.mod_feign.goods;

import com.siam.package_goods.entity.SmsLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/smsLogFeign")
public interface SmsLogFeignClient {

    @RequestMapping(value = "/getLastLog")
    SmsLog getLastLog(@RequestParam("mobile") String mobile, @RequestParam("type") String type, @RequestParam("minutes") int minutes);
}