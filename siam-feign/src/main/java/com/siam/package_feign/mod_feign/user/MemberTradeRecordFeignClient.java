package com.siam.package_feign.mod_feign.user;

import com.siam.package_user.entity.MemberTradeRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "user-siam", path = "/rest/memberTradeRecordFeign")
public interface MemberTradeRecordFeignClient {

    @RequestMapping(value = "/selectSumIncome")
    BigDecimal selectSumIncome(@RequestBody MemberTradeRecord memberTradeRecord);

    @RequestMapping(value = "/selectSumExpense")
    BigDecimal selectSumExpense(@RequestBody MemberTradeRecord memberTradeRecord);

    @RequestMapping(value = "/selectByPrimaryKey")
    MemberTradeRecord selectByPrimaryKey(@RequestParam("id") Integer id);

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MemberTradeRecord memberTradeRecord);

    @RequestMapping(value = "/selectByOutTradeNo")
    MemberTradeRecord selectByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo);

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MemberTradeRecord record);
}