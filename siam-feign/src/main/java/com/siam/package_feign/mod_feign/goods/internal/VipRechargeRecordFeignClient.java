package com.siam.package_feign.mod_feign.goods.internal;

import com.siam.package_goods.entity.internal.VipRechargeRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "goods-siam", path = "/rest/vipRechargeRecordFeign")
public interface VipRechargeRecordFeignClient {

    @RequestMapping(value = "/selectLastestPaid")
    VipRechargeRecord selectLastestPaid(@RequestParam("memberId") Integer memberId);

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    @RequestMapping(value = "/updateByPayNotify")
    void updateByPayNotify(@RequestParam("outTradeNo") String outTradeNo);

    @RequestMapping(value = "/insertSelective")
    void insertSelective(@RequestBody VipRechargeRecord vipRechargeRecord);
}