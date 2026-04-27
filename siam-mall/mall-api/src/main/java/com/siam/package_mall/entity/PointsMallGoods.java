package com.siam.package_mall.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_points_mall_goods")
 @Schema(description= "商品表")
public class PointsMallGoods {

    @TableField(exist = false)
    private Integer menuId;

    /**
     * 状态 1=待上架 2=已上架 3=已下架 4=售罄
     */
    public static final int STATUS_WAIT_ON_SHELF = 1;
    public static final int STATUS_ON_SHELF = 2;
    public static final int STATUS_OFF_SHELF = 3;
    public static final int STATUS_SELL_OUT = 4;

    private List<Integer> ids;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品主图")
    private String mainImage;

    @Schema(description = "商品子图")
    private String subImages;

    @Schema(description = "商品详情")
    private String detail;

    @Schema(description = "详情图片")
    private String detailImages;

    @Schema(description = "一口价")
    private BigDecimal price;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "是否热门")
    private Boolean isHot;

    @Schema(description = "是否新品")
    private Boolean isNew;

    @Schema(description = "状态 1=上架 2=下架")
    private Integer status;

    @Schema(description = "是否开启促销 0-否 1-是")
    private Boolean isSale;

    @Schema(description = "折扣价")
    private BigDecimal salePrice;

    @Schema(description = "月销量")
    private Integer monthlySales;

    @Schema(description = "累计销量")
    private Integer totalSales;

    @Schema(description = "累计评价")
    private Integer totalComments;

    @Schema(description = "优惠名称")
    private String preferentialName;

    @Schema(description = "包装费")
    private BigDecimal packingCharges;

    @Schema(description = "制作时长(分钟)")
    private BigDecimal productTime;

    @Schema(description = "兑换商品所需积分数量")
    private Integer exchangePoints;

    @Schema(description = "排序号")
    private Integer sortNumber;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    //页码
    @TableField(exist = false) private Integer pageNo = 1;

    //页面大小
    @TableField(exist = false) private Integer pageSize = 20;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages == null ? null : subImages.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages == null ? null : detailImages.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsSale() {
        return isSale;
    }

    public void setIsSale(Boolean isSale) {
        this.isSale = isSale;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(Integer monthlySales) {
        this.monthlySales = monthlySales;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public String getPreferentialName() {
        return preferentialName;
    }

    public void setPreferentialName(String preferentialName) {
        this.preferentialName = preferentialName == null ? null : preferentialName.trim();
    }

    public BigDecimal getPackingCharges() {
        return packingCharges;
    }

    public void setPackingCharges(BigDecimal packingCharges) {
        this.packingCharges = packingCharges;
    }

    public BigDecimal getProductTime() {
        return productTime;
    }

    public void setProductTime(BigDecimal productTime) {
        this.productTime = productTime;
    }

    public Integer getExchangePoints() {
        return exchangePoints;
    }

    public void setExchangePoints(Integer exchangePoints) {
        this.exchangePoints = exchangePoints;
    }
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}