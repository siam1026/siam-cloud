package com.siam.package_user.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.MemberBillingRecord;
import com.siam.package_user.model.example.MemberBillingRecordExample;
import com.siam.package_user.model.param.MemberBillingRecordParam;
import com.siam.package_user.service.MemberBillingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class MemberBillingRecordFeignProvider implements MemberBillingRecordFeignApi {

    @Autowired
    private MemberBillingRecordService memberBillingRecordService;

    public BasicResult insertSelective(MemberBillingRecord record){
        memberBillingRecordService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByExample(MemberBillingRecordParam param){
        MemberBillingRecordExample example = new MemberBillingRecordExample();
        MemberBillingRecordExample.Criteria criteria = example.createCriteria();
        if(param.getType() != null){
            criteria.andTypeEqualTo(param.getType());
        }
        if(param.getOperateType() != null){
            criteria.andOperateTypeEqualTo(param.getOperateType());
        }
        if(param.getCoinType() != null){
            criteria.andCoinTypeEqualTo(param.getCoinType());
        }
        if(param.getOrderId() != null){
            criteria.andOrderIdEqualTo(param.getOrderId());
        }
        if(param.getTypeList() != null){
            criteria.andTypeIn(param.getTypeList());
        }
        return BasicResult.success(memberBillingRecordService.selectByExample(example));
    }

    public BasicResult updateByPrimaryKeySelective(MemberBillingRecord record){
        memberBillingRecordService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    public BasicResult settledReward(){
        memberBillingRecordService.settledReward();
        return BasicResult.success();
    }
}