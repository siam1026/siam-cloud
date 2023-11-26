package com.siam.package_feign.mod_feign.user;

import com.siam.package_user.entity.MerchantBillingRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-siam", path = "/rest/merchantBillingRecordFeign")
public interface MerchantBillingRecordFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MerchantBillingRecord record);

    @RequestMapping(value = "/selectByPrimaryKey")
    MerchantBillingRecord selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MerchantBillingRecord record);
}