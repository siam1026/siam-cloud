package com.siam.package_user.feign;

import com.siam.package_user.entity.MerchantBillingRecord;
import com.siam.package_user.service.MerchantBillingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/merchantBillingRecordFeign")
public class MerchantBillingRecordFeign {

    @Autowired
    private MerchantBillingRecordService merchantBillingRecordService;

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody MerchantBillingRecord record){
        merchantBillingRecordService.insertSelective(record);
    }

    @RequestMapping(value = "/selectByPrimaryKey")
    MerchantBillingRecord selectByPrimaryKey(@RequestParam("id") Integer id){
        return merchantBillingRecordService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MerchantBillingRecord record){
        merchantBillingRecordService.updateByPrimaryKeySelective(record);
    }
}