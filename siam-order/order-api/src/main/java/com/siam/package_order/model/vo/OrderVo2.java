package com.siam.package_order.model.vo;

import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.entity.OrderRefundGoods;
import com.siam.package_order.entity.OrderRefundProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
 @Schema(description= "订单表")
public class OrderVo2 {

    private OrderRefund orderRefund;

    private List<OrderRefundGoods> orderRefundGoodsList;

    private List<OrderRefundProcess> orderRefundProcessList;
}