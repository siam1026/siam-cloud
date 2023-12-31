package com.siam.package_user.entity.internal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Data
@TableName("tb_vip_recharge_denomination_coupons_relation")
public class VipRechargeDenominationCouponsRelation {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer vipRechargeDenominationId;

    private Integer couponsId;

    private Integer giveQuantity;

    private Date createTime;

    String denominationCouponsRelationListStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVipRechargeDenominationId() {
        return vipRechargeDenominationId;
    }

    public void setVipRechargeDenominationId(Integer vipRechargeDenominationId) {
        this.vipRechargeDenominationId = vipRechargeDenominationId;
    }

    public Integer getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(Integer couponsId) {
        this.couponsId = couponsId;
    }

    public Integer getGiveQuantity() {
        return giveQuantity;
    }

    public void setGiveQuantity(Integer giveQuantity) {
        this.giveQuantity = giveQuantity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    //页码
    private Integer pageNo = 1;

    //页面大小
    private Integer pageSize = 20;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

}