package com.siam.package_user.feign;

import com.alibaba.fastjson.JSON;
import com.siam.package_user.entity.MemberTradeRecord;
import com.siam.package_user.service.MemberTradeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/memberTradeRecordFeign")
public class MemberTradeRecordFeign {

    @Autowired
    private MemberTradeRecordService memberTradeRecordService;

    @RequestMapping(value = "/selectSumIncome")
    public BigDecimal selectSumIncome(@RequestBody MemberTradeRecord memberTradeRecord){
        return memberTradeRecordService.selectSumIncome(memberTradeRecord, memberTradeRecord.getStartTime(), memberTradeRecord.getEndTime());
    }

    @RequestMapping(value = "/selectSumExpense")
    public BigDecimal selectSumExpense(@RequestBody MemberTradeRecord memberTradeRecord){
        return memberTradeRecordService.selectSumExpense(memberTradeRecord, memberTradeRecord.getStartTime(), memberTradeRecord.getEndTime());
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    MemberTradeRecord selectByPrimaryKey(@RequestParam("id") Integer id){
        return memberTradeRecordService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MemberTradeRecord memberTradeRecord){
        log.debug("\n\n》》》 insertSelective - memberTradeRecord = " + JSON.toJSONString(memberTradeRecord));
        memberTradeRecordService.insertSelective(memberTradeRecord);
    }

    @RequestMapping(value = "/selectByOutTradeNo")
    MemberTradeRecord selectByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo){
        return memberTradeRecordService.selectByOutTradeNo(outTradeNo);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MemberTradeRecord record){
        memberTradeRecordService.updateByPrimaryKeySelective(record);
    }
}