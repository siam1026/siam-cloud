package com.siam.package_rider.model.result;

import com.siam.package_rider.entity.Rider;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户表
 *
 * @author 暹罗
 */
@Data
public class RiderResult extends Rider {

    //token
    private String token;

    //是否为新用户
    private boolean isNewPeople;

    //openid
    private String openId;

    //今日获得余额
    private BigDecimal totayGainBalance;

    //今日获得积分
    private BigDecimal totayGainPoints;

    //昨日收益-邀请新用户注册奖励金额/佣金
    private BigDecimal yesterdayGainInviteRewardAmount;

    //优惠券数量
    private Integer couponsNumber;
}