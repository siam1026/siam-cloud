package com.siam.package_user.feign;

import com.siam.package_user.entity.MemberBillingRecord;
import com.siam.package_user.model.example.MemberBillingRecordExample;
import com.siam.package_user.service.MemberBillingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/memberBillingRecordFeign")
public class MemberBillingRecordFeign {

    @Autowired
    private MemberBillingRecordService memberBillingRecordService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MemberBillingRecord record){
        memberBillingRecordService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByExample")
    List<MemberBillingRecord> selectByExample(@RequestBody MemberBillingRecordExample example){
        return memberBillingRecordService.selectByExample(example);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MemberBillingRecord record){
        memberBillingRecordService.updateByPrimaryKeySelective(record);
    }

    @RequestMapping(value = "/settledReward")
    void settledReward(){
        memberBillingRecordService.settledReward();
    }
}