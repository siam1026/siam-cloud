package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.MerchantBillingRecord;
import com.siam.package_merchant.service.MerchantBillingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MerchantBillingRecordFeignProvider implements MerchantBillingRecordFeignApi {

    @Autowired
    private MerchantBillingRecordService merchantBillingRecordService;

    public BasicResult insertSelective(MerchantBillingRecord record){
        merchantBillingRecordService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(merchantBillingRecordService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(MerchantBillingRecord record){
        merchantBillingRecordService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }
}