package com.siam.package_goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_goods")
@ApiModel(value = "商品表")
public class Goods {

    /**
     * 状态 1=待上架 2=已上架 3=已下架 4=售罄
     */
    public static final int STATUS_WAIT_ON_SHELF = 1;
    public static final int STATUS_ON_SHELF = 2;
    public static final int STATUS_OFF_SHELF = 3;
    public static final int STATUS_SELL_OUT = 4;

    @TableField(exist = false)
    String position;

    //页码
    @TableField(exist = false) private Integer pageNo = 1;

    //页面大小
    @TableField(exist = false) private Integer pageSize = 20;

    // ------------------------------------------------------------------------------------

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(notes = "店铺id")
    private Integer shopId;

    @ApiModelProperty(notes = "商品名称")
    private String name;

    @ApiModelProperty(notes = "商品主图")
    private String mainImage;

    @ApiModelProperty(notes = "商品子图")
    private String subImages;

    @ApiModelProperty(notes = "商品详情")
    private String detail;

    @ApiModelProperty(notes = "详情图片")
    private String detailImages;

    @ApiModelProperty(notes = "一口价")
    private BigDecimal price;

    @ApiModelProperty(notes = "库存")
    private Integer stock;

    @ApiModelProperty(notes = "是否热门")
    private Boolean isHot;

    @ApiModelProperty(notes = "是否新品")
    private Boolean isNew;

    @ApiModelProperty(notes = "状态 1=上架 2=下架")
    private Integer status;

    @ApiModelProperty(notes = "是否开启促销 0-否 1-是")
    private Boolean isSale;

    @ApiModelProperty(notes = "折扣价")
    private BigDecimal salePrice;

    @ApiModelProperty(notes = "月销量")
    private Integer monthlySales;

    @ApiModelProperty(notes = "累计销量")
    private Integer totalSales;

    @ApiModelProperty(notes = "累计评价")
    private Integer totalComments;

    @ApiModelProperty(notes = "优惠名称")
    private String preferentialName;

    @ApiModelProperty(notes = "包装费")
    private BigDecimal packingCharges;

    @ApiModelProperty(notes = "制作时长(分钟)")
    private BigDecimal productTime;

    @ApiModelProperty(notes = "兑换商品所需积分数量")
    private Integer exchangePoints;

    @ApiModelProperty(notes = "排序号")
    private Integer sortNumber;

    @ApiModelProperty(notes = "打印机id(多个用逗号隔开)")
    private String printerId;

    @ApiModelProperty(notes = "打印次数")
    private Integer printNum;

    @ApiModelProperty(notes = "单位id")
    private Long unitId;

    private String unitName;

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;
}