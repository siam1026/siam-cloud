package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.model.example.MerchantWithdrawRecordExample;
import com.siam.package_user.service.MerchantWithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/merchantWithdrawRecordFeign")
public class MerchantWithdrawRecordFeign {

    @Autowired
    private MerchantWithdrawRecordService merchantWithdrawRecordService;

    @RequestMapping(value = "/countByAuditStatus")
    public BasicResult countByAuditStatus(@RequestBody int auditStatus){
        MerchantWithdrawRecordExample merchantWithdrawExample = new MerchantWithdrawRecordExample();
        merchantWithdrawExample.createCriteria().andAuditStatusEqualTo(auditStatus);
        return BasicResult.success(merchantWithdrawRecordService.countByExample(merchantWithdrawExample));
    }
}