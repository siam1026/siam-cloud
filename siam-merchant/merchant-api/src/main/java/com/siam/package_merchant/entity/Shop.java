package com.siam.package_merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_shop")
@ApiModel(value = "门店表")
public class Shop {

    /* ##################################### START 扩展字段 #################################### */

    @TableField(exist = false)
    String position;

    @TableField(exist = false)
    private List<Integer> ids;

    //开始日期
    @SerializedName("startCreateTime")
    @TableField(exist = false)
    private Date startCreateTime;

    //结束日期
    @SerializedName("endCreateTime")
    @TableField(exist = false)
    private Date endCreateTime;

    /*//配送距离上限(KM)
    private BigDecimal deliveryDistanceLimit;*/

    //定位的经度
    @TableField(exist = false)
    private BigDecimal positionLongitude;

    //定位的维度
    @TableField(exist = false)
    private BigDecimal positionLatitude;

    //页码
    @TableField(exist = false) private Integer pageNo = 1;

    //页面大小
    @TableField(exist = false) private Integer pageSize = 20;

    /* ##################################### END 扩展字段 #################################### */

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Start 特殊字段标识 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    //店铺id列表
    @TableField(exist = false)
    private List<Integer> shopIdList;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ End ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer merchantId;

    private String name;

    private String code;

    private String province;

    private String city;

    private String area;

    private String street;

    private Boolean isOperating;

    private String startTime;

    private String endTime;

    private String managePrimary;

    private String manageMinor;

    private String shopImg;

    private String shopWithinImg;

    private String shopLogoImg;

    private String certificateType1;

    private String certificateImg1;

    private String certificateType2;

    private String certificateImg2;

    private String specialType;

    private String specialImg;

    private Integer auditStatus;

    private String auditReason;

    @SerializedName("auditTime")
    private Date auditTime;

    private String takeOutPhone;

    private String contactRealname;

    private String contactPhone;

    private String announcement;

    private BigDecimal startDeliveryPrice;

    private BigDecimal deliveryStartingPrice;

    private BigDecimal deliveryKilometerPrice;

    private BigDecimal deliveryDistanceLimit;

    private BigDecimal serviceRating;

    private String businessLicense;

    private String idCardFrontSide;

    private String idCardBackSide;

    private Integer status;

    private BigDecimal reducedDeliveryPrice;

    private Boolean isOpenOrderAudio;

    private Boolean isOpenLocalPrint;

    private Boolean isOpenCloudPrint;

    private String firstPoster;

    private String houseNumber;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Integer kitchenTotalOrderPrinterId;

    private Integer checkoutPrinterId;

    @SerializedName("createTime")
    private Date createTime;

    @SerializedName("updateTime")
    private Date updateTime;

    private String briefIntroduction;
}