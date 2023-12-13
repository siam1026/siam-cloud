package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.MemberTradeRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "user-siam")
public interface MemberTradeRecordFeignApi {

    @PostMapping(value = "/api/memberTradeRecord/selectSumIncome")
    BasicResult<BigDecimal> selectSumIncome(@RequestBody MemberTradeRecord memberTradeRecord);

    @PostMapping(value = "/api/memberTradeRecord/selectSumExpense")
    BasicResult<BigDecimal> selectSumExpense(@RequestBody MemberTradeRecord memberTradeRecord);

    @PostMapping(value = "/api/memberTradeRecord/selectByPrimaryKey")
    BasicResult<MemberTradeRecord> selectByPrimaryKey(@RequestParam("id") Integer id);

    @PostMapping(value = "/api/memberTradeRecord/insertSelective")
    BasicResult<Integer> insertSelective(@RequestBody MemberTradeRecord memberTradeRecord);

    @PostMapping(value = "/api/memberTradeRecord/selectByOutTradeNo")
    BasicResult<MemberTradeRecord> selectByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo);

    @PostMapping(value = "/api/memberTradeRecord/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody MemberTradeRecord record);
}