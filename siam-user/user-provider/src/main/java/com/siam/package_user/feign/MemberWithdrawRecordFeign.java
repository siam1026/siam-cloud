package com.siam.package_user.feign;

import com.siam.package_user.service.MemberWithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/memberWithdrawRecordFeign")
public class MemberWithdrawRecordFeign {

    @Autowired
    private MemberWithdrawRecordService memberWithdrawRecordService;

    @RequestMapping(value = "/autoPayment")
    public void autoPayment(){
        memberWithdrawRecordService.autoPayment();
    }
}