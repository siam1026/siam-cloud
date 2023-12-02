package com.siam.package_goods.feign.internal;

import com.siam.package_goods.entity.internal.VipRechargeRecord;
import com.siam.package_goods.service.internal.VipRechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/vipRechargeRecordFeign")
public class VipRechargeRecordFeign {

    @Autowired
    private VipRechargeRecordService vipRechargeRecordService;


    @RequestMapping(value = "/selectLastestPaid")
    VipRechargeRecord selectLastestPaid(@RequestParam("memberId") Integer memberId){
        return vipRechargeRecordService.selectLastestPaid(memberId);
    }

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    @RequestMapping(value = "/updateByPayNotify")
    void updateByPayNotify(@RequestParam("outTradeNo") String outTradeNo){
        vipRechargeRecordService.updateByPayNotify(outTradeNo);
    }

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody VipRechargeRecord vipRechargeRecord){
        vipRechargeRecordService.insertSelective(vipRechargeRecord);
    }
}