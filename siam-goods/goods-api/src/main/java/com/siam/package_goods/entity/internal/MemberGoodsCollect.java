package com.siam.package_goods.entity.internal;
import com.baomidou.mybatisplus.annotation.IdType; import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("tb_member_goods_collect")
 @Schema(description= "用户收藏表")
public class MemberGoodsCollect {

    List<Integer> goodsIdList;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private Integer memberId;

    @Schema(description = "店铺id")
    private Integer shopId;

    @Schema(description = "商品id")
    private Integer goodsId;

    @Schema(description = "商品是否有效 0=无效 1=有效")
    private Boolean isGoodsExists;

    @Schema(description = "商品是否购买 0=未购买 1=已购买")
    private Boolean isBuy;

    @Schema(description = "收藏类型 1=外卖自提 2=积分商城")
    private Integer type;

    @Schema(description = "创建时间")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Boolean getIsGoodsExists() {
        return isGoodsExists;
    }

    public void setIsGoodsExists(Boolean isGoodsExists) {
        this.isGoodsExists = isGoodsExists;
    }

    public Boolean getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}