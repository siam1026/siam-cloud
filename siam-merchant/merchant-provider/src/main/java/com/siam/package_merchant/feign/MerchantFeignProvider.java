package com.siam.package_merchant.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_merchant.model.example.MerchantBillingRecordExample;
import com.siam.package_merchant.model.example.MerchantExample;
import com.siam.package_merchant.model.param.MerchantParam;
import com.siam.package_merchant.service.MerchantService;
import com.siam.package_user.model.example.MemberBillingRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MerchantFeignProvider implements MerchantFeignApi {

    @Autowired
    private MerchantService merchantService;

//    public BasicResult countByExample(MerchantParam param) {
//        MerchantExample example = new MerchantExample();
//        MerchantExample.Criteria criteria = example.createCriteria();
//        if(order.getShopId() != null){
//            criteria.andShopIdEqualTo(order.getShopId());
//        }
//        return BasicResult.success(merchantService.countByExample(example));
//    }

    public BasicResult insertSelective(Merchant record){
        merchantService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByExample(MerchantParam param) {
        MerchantExample example = new MerchantExample();
        MerchantExample.Criteria criteria = example.createCriteria();
        if(param.getShopIdList() != null){
            criteria.andShopIdIn(param.getShopIdList());
        }
        return BasicResult.success(merchantService.selectByExample(example));
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(merchantService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(Merchant record){
        merchantService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByMobile(@RequestParam("mobile") String mobile){
        return BasicResult.success(merchantService.selectByMobile(mobile));
    }
}