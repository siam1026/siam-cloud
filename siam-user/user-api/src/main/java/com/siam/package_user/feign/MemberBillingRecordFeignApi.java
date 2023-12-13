package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.MemberBillingRecord;
import com.siam.package_user.model.example.MemberBillingRecordExample;
import com.siam.package_user.model.param.MemberBillingRecordParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "user-siam")
public interface MemberBillingRecordFeignApi {

    @PostMapping(value = "/api/memberBillingRecord/insertSelective")
    BasicResult insertSelective(@RequestBody MemberBillingRecord record);

    @PostMapping(value = "/api/memberBillingRecord/selectByExample")
    BasicResult<List<MemberBillingRecord>> selectByExample(@RequestBody MemberBillingRecordParam param);

    @PostMapping(value = "/api/memberBillingRecord/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody MemberBillingRecord record);

    @PostMapping(value = "/api/memberBillingRecord/settledReward")
    BasicResult settledReward();
}