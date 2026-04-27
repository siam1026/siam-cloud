package com.siam.package_mall.model.dto;

import com.siam.package_mall.entity.PointsMallGoodsSpecification;
import io.swagger.v3.oas.annotations.media.Schema;

public class PointsMallGoodsSpecificationDto11 extends PointsMallGoodsSpecification {
    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品主图")
    private String goodsMainImage;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsMainImage() {
        return goodsMainImage;
    }

    public void setGoodsMainImage(String goodsMainImage) {
        this.goodsMainImage = goodsMainImage;
    }
}