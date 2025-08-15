package com.siam.package_util.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderPrintDto {

    private String orderNo;

    private BigDecimal packingCharges;

    private BigDecimal deliveryFee;

    private String queueNo;

    private int printNum;

    private Integer fullReductionRuleId;

    private String fullReductionRuleDescription;

    private Integer couponsId;

    private String couponsDescription;

    private String contactRealName;

    private String tableName;

    private String shopName;

    private String shopAddress;

    private String shopContactNumber;

    private String paymentTime;

    private String orderTime;

    private String payTypeName;

    private BigDecimal totalPrice;

    private BigDecimal actualPrice;

    private String remark;

    private int goodsTotalQuantity;

    private String printerSn;
}