package com.siam.package_feign.mod_feign.user;

import com.siam.package_user.entity.Merchant;
import com.siam.package_user.model.example.MerchantExample;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "user-siam", path = "/rest/merchantFeign")
public interface MerchantFeignClient {

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody MerchantExample example);

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody Merchant record);

    @RequestMapping(value = "/selectByExample")
    List<Merchant> selectByExample(@RequestBody MerchantExample example);

    @RequestMapping(value = "/selectByPrimaryKey")
    Merchant selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Merchant record);

    @RequestMapping(value = "/selectByMobile")
    Merchant selectByMobile(@RequestParam("mobile") String mobile);
}