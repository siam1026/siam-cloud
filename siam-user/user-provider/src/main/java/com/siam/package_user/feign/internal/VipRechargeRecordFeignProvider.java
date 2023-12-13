package com.siam.package_user.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.internal.VipRechargeRecord;
import com.siam.package_user.service.internal.VipRechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class VipRechargeRecordFeignProvider implements VipRechargeRecordFeignApi {

    @Autowired
    private VipRechargeRecordService vipRechargeRecordService;


    public BasicResult selectLastestPaid(@RequestParam("memberId") Integer memberId){
        return BasicResult.success(vipRechargeRecordService.selectLastestPaid(memberId));
    }

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    public BasicResult updateByPayNotify(@RequestParam("outTradeNo") String outTradeNo){
        vipRechargeRecordService.updateByPayNotify(outTradeNo);
        return BasicResult.success();
    }

    public BasicResult insertSelective(VipRechargeRecord vipRechargeRecord){
        vipRechargeRecordService.insertSelective(vipRechargeRecord);
        return BasicResult.success();
    }
}