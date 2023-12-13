package com.siam.package_util.executor;

import com.siam.package_common.annoation.ScheduledTaskAnnotation;
import com.siam.package_common.constant.ScheduledTaskConst;
import com.siam.package_goods.feign.GoodsFeignApi;
import com.siam.package_merchant.feign.MerchantFeignApi;
import com.siam.package_order.feign.OrderFeignApi;
import com.siam.package_promotion.feign.CouponsMemberRelationFeignApi;
import com.siam.package_user.feign.MemberBillingRecordFeignApi;
import com.siam.package_user.feign.MemberFeignApi;
import com.siam.package_user.feign.MemberWithdrawRecordFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 定时任务执行器
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class ScheduledTaskExecutor {

    @Autowired
    private OrderFeignApi orderFeignApi;

    @Autowired
    private CouponsMemberRelationFeignApi couponsMemberRelationFeignApi;

    @Autowired
    private GoodsFeignApi goodsFeignApi;

    @Autowired
    private MemberFeignApi memberFeignApi;

    @Autowired
    private MerchantFeignApi merchantFeignApi;

    @Autowired
    private MemberWithdrawRecordFeignApi memberWithdrawRecordFeignApi;

    @Autowired
    private MemberBillingRecordFeignApi memberBillingRecordFeignApi;

    /**
     * 每隔1分钟检测订单超时操作
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.CLOSE_OVERDUE_ORDER)
    public void updateByCloseOverdueOrder_task(){
        //每隔一分钟检测，如果有符合条件的用户提现记录，则进行自动打款
        memberWithdrawRecordFeignApi.autoPayment();
    }

    /**
     * 每天0点检测逾期的优惠卷
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.CHECK_OVERDUE_COUPONS)
    public void updateByCheckOverdueCoupons_task(){
        couponsMemberRelationFeignApi.updateOverdue();
    }

    /**
     * 每月1号0点清空所有商品的月销量
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.MONTHLY_SALES_RESET)
    public void updateMonthlySalesReset_task(){
        goodsFeignApi.monthlySalesReset();
    }

    /**
     * 每天凌晨0点5分将新用户的"是否需要弹出新人引导提示"字段值改成是
     */
    @Scheduled(cron = "0 5 0 * * ?")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.UPDATE_ISREMINDNEWPEOPLE_OF_MEMBER)
    public void updateIsRemindNewPeopleOfMember_task(){
        memberFeignApi.updateIsRemindNewPeople();
    }

    /**
     * 每天凌晨0点10分修改用户的是否需要请求授权服务通知状态
     */
    @Scheduled(cron = "0 10 0 * * ?")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.UPDATE_ISREQUESTWXNOTIFY_OF_MEMBER)
    public void updateIsRequestWxNotifyOfMember_task(){
        memberFeignApi.updateIsRequestWxNotify();
    }

    /**
     * 每天凌晨0点15分检测给商家发放用户下单资金
     */
    @Scheduled(cron = "0 15 0 * * ?")
    /*@Scheduled(cron = "0 49 3 * * ?")*/
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.UPDATE_PAYORDERFROZENBALANCE_OF_MERCHANT)
    public void updatePayOrderFrozenBalanceOfMerchant_task(){
        orderFeignApi.updatePayOrderFrozenBalanceOfMerchant();
    }

    /**
     * 每天凌晨0点30分处理综合事件
     */
    @Scheduled(cron = "0 30 0 * * ?")
    /*@Scheduled(cron = "0 49 3 * * ?")*/
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.HANDLE_GENERAL_EVENT)
    public void syncPointsMallOrderLogisticsInfo_task() throws IOException {
        //此处为最后一个时间段
        //查询微信公众号openid
        memberFeignApi.queryWxPublicPlatformOpenId();

        //对未到账的积分、佣金奖励进行结算
        //结算时间：外卖系统-下单7天后，积分商城-下单25天后
        memberBillingRecordFeignApi.settledReward();
    }

    /**
     * 每周三下午三点发送优惠卷即将过期提醒
     */
    @Scheduled(cron = "0 0 15 ? * WED")
    @ScheduledTaskAnnotation(code = ScheduledTaskConst.COUPONS_OVERDUE_SMS_REMINDER)
    public void couponsOverdueSMSReminderTask(){
        couponsMemberRelationFeignApi.overdueSMSReminder();
    }
}