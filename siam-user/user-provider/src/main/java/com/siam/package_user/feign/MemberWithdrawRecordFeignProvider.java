package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.service.MemberWithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MemberWithdrawRecordFeignProvider implements MemberWithdrawRecordFeignApi {

    @Autowired
    private MemberWithdrawRecordService memberWithdrawRecordService;

    public BasicResult autoPayment(){
        memberWithdrawRecordService.autoPayment();
        return BasicResult.success();
    }
}