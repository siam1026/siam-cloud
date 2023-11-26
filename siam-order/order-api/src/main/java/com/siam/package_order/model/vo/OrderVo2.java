package com.siam.package_order.model.vo;

import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.entity.OrderRefundGoods;
import com.siam.package_order.entity.OrderRefundProcess;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "订单表")
public class OrderVo2 {

    private OrderRefund orderRefund;

    private List<OrderRefundGoods> orderRefundGoodsList;

    private List<OrderRefundProcess> orderRefundProcessList;
}