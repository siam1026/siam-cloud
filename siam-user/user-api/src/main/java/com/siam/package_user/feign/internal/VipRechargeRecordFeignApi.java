package com.siam.package_user.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.internal.VipRechargeRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-siam")
public interface VipRechargeRecordFeignApi {

    @PostMapping(value = "/api/vipRechargeRecord/selectLastestPaid")
    BasicResult<VipRechargeRecord> selectLastestPaid(@RequestParam("memberId") Integer memberId);

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    @PostMapping(value = "/api/vipRechargeRecord/updateByPayNotify")
    BasicResult updateByPayNotify(@RequestParam("outTradeNo") String outTradeNo);

    @PostMapping(value = "/api/vipRechargeRecord/insertSelective")
    BasicResult insertSelective(@RequestBody VipRechargeRecord vipRechargeRecord);
}