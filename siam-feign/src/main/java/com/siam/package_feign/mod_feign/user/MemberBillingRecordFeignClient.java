package com.siam.package_feign.mod_feign.user;

import com.siam.package_user.entity.MemberBillingRecord;
import com.siam.package_user.model.example.MemberBillingRecordExample;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "user-siam", path = "/rest/memberBillingRecordFeign")
public interface MemberBillingRecordFeignClient {

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MemberBillingRecord record);

    @RequestMapping(value = "/selectByExample")
    List<MemberBillingRecord> selectByExample(@RequestBody MemberBillingRecordExample example);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MemberBillingRecord record);

    @RequestMapping(value = "/settledReward")
    void settledReward();
}