package com.siam.package_user.feign;

import com.alibaba.fastjson.JSON;
import com.siam.package_common.entity.BasicResult;
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
public class MemberTradeRecordFeignProvider implements MemberTradeRecordFeignApi {

    @Autowired
    private MemberTradeRecordService memberTradeRecordService;

    public BasicResult selectSumIncome(MemberTradeRecord memberTradeRecord){
        return BasicResult.success(memberTradeRecordService.selectSumIncome(memberTradeRecord, memberTradeRecord.getStartTime(), memberTradeRecord.getEndTime()));
    }

    public BasicResult selectSumExpense(MemberTradeRecord memberTradeRecord){
        return BasicResult.success(memberTradeRecordService.selectSumExpense(memberTradeRecord, memberTradeRecord.getStartTime(), memberTradeRecord.getEndTime()));
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(memberTradeRecordService.selectByPrimaryKey(id));
    }

    public BasicResult insertSelective(MemberTradeRecord memberTradeRecord){
        log.debug("\n\n》》》 insertSelective - memberTradeRecord = " + JSON.toJSONString(memberTradeRecord));
        memberTradeRecordService.insertSelective(memberTradeRecord);
        return BasicResult.success(memberTradeRecord.getId());
    }

    public BasicResult selectByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo){
        return BasicResult.success(memberTradeRecordService.selectByOutTradeNo(outTradeNo));
    }

    public BasicResult updateByPrimaryKeySelective(MemberTradeRecord record){
        memberTradeRecordService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }
}