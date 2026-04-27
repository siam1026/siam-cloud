package com.siam.package_mall.model.dto;

import com.siam.package_mall.entity.PointsMallGoodsSpecificationOption;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class PointsMallGoodsSpecificationOptionDto extends PointsMallGoodsSpecificationOption {
    @Schema(description = "规格名称")
    private String specificationName;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品主图")
    private String goodsMainImage;

    public PointsMallGoodsSpecificationOptionDto() {
    }

    public PointsMallGoodsSpecificationOptionDto(String specificationName, String name, BigDecimal price) {
        this.specificationName = specificationName;
        super.setName(name);
        super.setPrice(price);
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

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