package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.MerchantBillingRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "merchant-siam")
public interface MerchantBillingRecordFeignApi {

    @PostMapping(value = "/api/merchantBillingRecord/insertSelective")
    BasicResult insertSelective(@RequestBody MerchantBillingRecord record);

    @PostMapping(value = "/api/merchantBillingRecord/selectByPrimaryKey")
    BasicResult selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/merchantBillingRecord/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody MerchantBillingRecord record);
}