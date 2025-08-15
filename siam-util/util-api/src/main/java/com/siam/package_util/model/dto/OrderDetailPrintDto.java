package com.siam.package_util.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailPrintDto {

    private String goodsName;

    private int number;

    private String unitName;

    private String goodsWeight;

    private String goodsSpecs;

    private BigDecimal subtotal;

//    private String title;

    private int printNum;

    private String printerName;

    private String printerSn;

    private Integer printerId;
}