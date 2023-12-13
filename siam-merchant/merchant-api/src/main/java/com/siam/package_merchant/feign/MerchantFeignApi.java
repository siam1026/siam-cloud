package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_merchant.model.example.MerchantExample;
import com.siam.package_merchant.model.param.MerchantParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "merchant-siam")
public interface MerchantFeignApi {

//    @PostMapping(value = "/api/merchant/countByExample")
//    BasicResult countByExample(@RequestBody MerchantParam param);

    @PostMapping(value = "/api/merchant/insertSelective")
    BasicResult insertSelective(@RequestBody Merchant record);

    @PostMapping(value = "/api/merchant/selectByExample")
    BasicResult<List<Merchant>> selectByExample(@RequestBody MerchantParam param);

    @PostMapping(value = "/api/merchant/selectByPrimaryKey")
    BasicResult<Merchant> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/merchant/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody Merchant record);

    @PostMapping(value = "/api/merchant/selectByMobile")
    BasicResult selectByMobile(@RequestParam("mobile") String mobile);
}