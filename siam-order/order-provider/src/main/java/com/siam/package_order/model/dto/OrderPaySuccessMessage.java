package com.siam.package_order.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付成功事务消息体
 */
@Data
public class OrderPaySuccessMessage implements Serializable {
    private Integer orderId;
    private String orderNo;
    private Integer shopId;
    private Integer memberId;
    private Integer shoppingWay;
    private BigDecimal actualPrice;
}
