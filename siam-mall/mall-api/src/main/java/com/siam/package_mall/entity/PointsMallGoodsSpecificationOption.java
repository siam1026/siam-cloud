package com.siam.package_mall.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_points_mall_goods_specification_option")
 @Schema(description= "商品规格选项表")
public class PointsMallGoodsSpecificationOption {

    private List<Integer> ids;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "商品id")
    private Integer goodsId;

    @Schema(description = "商品规格id")
    private Integer goodsSpecificationId;

    @Schema(description = "商品商品规格选项名称")
    private String name;

    @Schema(description = "单价/加价金额")
    private BigDecimal price;

    @Schema(description = "库存 1=有货 2=无货")
    private Integer stock;

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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsSpecificationId() {
        return goodsSpecificationId;
    }

    public void setGoodsSpecificationId(Integer goodsSpecificationId) {
        this.goodsSpecificationId = goodsSpecificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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