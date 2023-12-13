package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.model.example.MerchantWithdrawRecordExample;
import com.siam.package_merchant.service.MerchantWithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MerchantWithdrawRecordFeignProvider implements MerchantWithdrawRecordFeignApi {

    @Autowired
    private MerchantWithdrawRecordService merchantWithdrawRecordService;

    public BasicResult countByAuditStatus(int auditStatus){
        MerchantWithdrawRecordExample merchantWithdrawExample = new MerchantWithdrawRecordExample();
        merchantWithdrawExample.createCriteria().andAuditStatusEqualTo(auditStatus);
        return BasicResult.success(merchantWithdrawRecordService.countByExample(merchantWithdrawExample));
    }
}