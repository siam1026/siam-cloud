package com.siam.package_order.feign;

import com.siam.package_common.constant.Quantity;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.controller.member.WxPayService;
import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.model.example.OrderRefundExample;
import com.siam.package_order.service.OrderRefundService;
import com.siam.package_weixin_basic.util.WxMD5Util;
import com.siam.package_weixin_pay.entity.TransfersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class WxPayFeignProvider implements WxPayFeignApi {

    @Autowired
    private WxPayService wxPayService;

    @Override
    public BasicResult<Boolean> payToBalance(TransfersDto model) {
        return BasicResult.success(wxPayService.payToBalance(model));
    }
}