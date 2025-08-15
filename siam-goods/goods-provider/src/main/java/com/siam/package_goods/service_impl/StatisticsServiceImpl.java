package com.siam.package_goods.service_impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.siam.package_common.constant.Quantity;
import com.siam.package_common.util.DateUtilsExtend;
import com.siam.package_common.util.DateUtilsPlus;
import com.siam.package_goods.entity.Goods;
import com.siam.package_goods.model.example.GoodsExample;
import com.siam.package_goods.model.param.StatisticsParam;
import com.siam.package_goods.service.GoodsService;
import com.siam.package_goods.service.ShoppingCartService;
import com.siam.package_goods.service.StatisticsService;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_merchant.feign.MerchantWithdrawRecordFeignApi;
import com.siam.package_merchant.feign.ShopChangeRecordFeignApi;
import com.siam.package_merchant.feign.ShopFeignApi;
import com.siam.package_merchant.model.example.ShopChangeRecordExample;
import com.siam.package_merchant.model.example.ShopExample;
import com.siam.package_merchant.model.param.ShopChangeRecordParam;
import com.siam.package_merchant.model.param.ShopParam;
import com.siam.package_order.entity.OrderRefund;
import com.siam.package_order.feign.OrderFeignApi;
import com.siam.package_order.feign.OrderRefundFeignApi;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_user.entity.Member;
import com.siam.package_user.entity.MemberTradeRecord;
import com.siam.package_user.feign.MemberFeignApi;
import com.siam.package_user.feign.MemberTradeRecordFeignApi;
import com.siam.package_user.util.TokenUtil;
import com.siam.package_util.feign.SystemUsageRecordFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private OrderFeignApi orderFeignApi;

    @Autowired
    private MemberTradeRecordFeignApi memberTradeRecordFeignApi;

    @Autowired
    private ShopFeignApi shopFeignApi;

    @Autowired
    private ShopChangeRecordFeignApi shopChangeRecordFeignApi;

    @Autowired
    private SystemUsageRecordFeignApi systemUsageRecordFeignApi;

    @Autowired
    private OrderRefundFeignApi orderRefundFeignApi;

    @Autowired
    private MerchantWithdrawRecordFeignApi merchantWithdrawRecordFeignApi;

    @Autowired
    private MemberFeignApi memberFeignApi;

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public Map todayStatisticByAdmin(StatisticsParam param) {
        Map resultMap = new HashMap();

        //订单表筛选条件-所有商家
        OrderParam order = new OrderParam();

        //今日支付订单、今日支付金额、待完成订单、待处理退款
        order.setStartTime(DateUtilsExtend.getDayBegin());
        order.setEndTime(DateUtilsExtend.getDayEnd());
        int dayCountPaid = orderFeignApi.selectCountCompleted(order).getData();
        BigDecimal daySumActualPrice = orderFeignApi.selectSumActualPrice(order).getData();
        int unCompletedNum = orderFeignApi.selectCountUnCompleted(order).getData();
        int waitHandleRefundNum = orderFeignApi.selectCountWaitHandle(order).getData();

        //总帐余额 = 累计进账 - 累计出账
        //今日进账 = 所有微信支付入口汇总的金额；退款金额不做计算；余额支付、积分支付这种不算
        //今日出账 = 佣金提现 + 商户提现 + 退款金额之类的所有由微信支出的汇总；余额和积分的支付也算
        MemberTradeRecord memberTradeRecord = new MemberTradeRecord();
        memberTradeRecord.setStartTime(DateUtilsExtend.getDayBegin());
        memberTradeRecord.setEndTime(DateUtilsExtend.getDayEnd());
        BigDecimal daySumIncome = memberTradeRecordFeignApi.selectSumIncome(memberTradeRecord).getData();
        BigDecimal daySumExpense = memberTradeRecordFeignApi.selectSumExpense(memberTradeRecord).getData();
        //退款金额--微信/支付宝
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setStartTime(DateUtilsExtend.getDayBegin());
        orderRefund.setEndTime(DateUtilsExtend.getDayEnd());
        BigDecimal daySumRefundAmount = orderRefundFeignApi.selectSumRefundAmount(orderRefund).getData();
        daySumExpense = daySumExpense.add(daySumRefundAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        //退款金额--余额/积分
        BigDecimal daySumRefundAmountByPlatformCoin = orderRefundFeignApi.selectSumRefundAmountByPlatformCoin(orderRefund).getData();
        daySumExpense = daySumExpense.subtract(daySumRefundAmountByPlatformCoin).setScale(2, BigDecimal.ROUND_HALF_UP);

        memberTradeRecord.setStartTime(new Date("1970/1/1 00:00:00"));
        memberTradeRecord.setEndTime(new Date("2100/1/1 00:00:00"));
        BigDecimal totalSumIncome = memberTradeRecordFeignApi.selectSumIncome(memberTradeRecord).getData();
        BigDecimal totalSumExpense = memberTradeRecordFeignApi.selectSumExpense(memberTradeRecord).getData();
        //退款金额--微信/支付宝
        orderRefund.setStartTime(new Date("1970/1/1 00:00:00"));
        orderRefund.setEndTime(new Date("2100/1/1 00:00:00"));
        BigDecimal totalSumRefundAmount = orderRefundFeignApi.selectSumRefundAmount(orderRefund).getData();
        totalSumExpense = totalSumExpense.add(totalSumRefundAmount);
        //退款金额--余额/积分
        BigDecimal totalSumRefundAmountByPlatformCoin = orderRefundFeignApi.selectSumRefundAmountByPlatformCoin(orderRefund).getData();
        totalSumExpense = totalSumExpense.subtract(totalSumRefundAmountByPlatformCoin).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal balance = totalSumIncome.subtract(totalSumExpense).setScale(2, BigDecimal.ROUND_HALF_UP);

        //商品支付金额、配送费金额、退款金额、配送费退款金额 -- 针对于所有店铺、现金/余额都算
        Map<String, Object> orderSumField = orderFeignApi.selectSumField(order).getData();
        Map<String, Object> orderRefundSumField = orderRefundFeignApi.selectSumField(new OrderRefund()).getData();
        BigDecimal totalActualPrice = new BigDecimal((Double) orderSumField.get("totalActualPrice"));
        BigDecimal totalDeliveryFee = new BigDecimal((Double) orderSumField.get("totalDeliveryFee"));
        BigDecimal totalRefundAmount = new BigDecimal((Double) orderRefundSumField.get("totalRefundAmount"));
        BigDecimal totalRefundDeliveryFee = new BigDecimal((Double) orderRefundSumField.get("totalRefundDeliveryFee"));

        //待处理开店申请、待处理提现申请、待处理变更资料申请、待处理退款申请
        ShopParam shopParam = new ShopParam();
        shopParam.setAuditStatus(Quantity.INT_1);
        int handleShopCount = shopFeignApi.countByExample(shopParam).getData();

        int handleMerchantWithdrawCount = merchantWithdrawRecordFeignApi.countByAuditStatus(Quantity.INT_1).getData();

        ShopChangeRecordParam changeRecordParam = new ShopChangeRecordParam();
        changeRecordParam.setAuditStatus(Quantity.INT_1);
        int handleShopChangeCount = shopChangeRecordFeignApi.countByExample(changeRecordParam).getData();

        OrderRefund orderRefundCount = new OrderRefund();
        orderRefundCount.setStatus(Quantity.INT_4);
        int handleOrderRefundCount = orderRefundFeignApi.countByExample(orderRefundCount).getData();

        //商家总览：已下架、已上架、全部商家
        shopParam = new ShopParam();
        shopParam.setStatus(Quantity.INT_3);
        int underShelfShopCount = shopFeignApi.countByExample(shopParam).getData();

        shopParam = new ShopParam();
        shopParam.setStatus(Quantity.INT_2);
        int onShelfShopCount = shopFeignApi.countByExample(shopParam).getData();

        /*List filterList = new ArrayList<>();
        filterList.add(Quantity.INT_1);
        filterList.add(Quantity.INT_2);
        filterList.add(Quantity.INT_3);*/
        shopParam = new ShopParam();
        shopParam.setAuditStatus(Quantity.INT_2);
        int allShopCount = shopFeignApi.countByExample(shopParam).getData();

        //用户总览：今日新增、昨日新增、本月新增、会员总数
        int dayMemberCount = memberFeignApi.selectCountRegister(DateUtilsExtend.getDayBegin(), DateUtilsExtend.getDayEnd()).getData();
        int yesterdayMemberCount = memberFeignApi.selectCountRegister(DateUtilsExtend.getBeginDayOfYesterday(), DateUtilsExtend.getEndDayOfYesterDay()).getData();
        int thisMonthMemberCount = memberFeignApi.selectCountRegister(DateUtilsExtend.getBeginDayOfMonth(), DateUtilsExtend.getEndDayOfMonth()).getData();
        int allMemberCount = memberFeignApi.countByExample(new Member()).getData();

        resultMap.put("dayCountPaid", dayCountPaid);
        resultMap.put("daySumActualPrice", daySumActualPrice);
        resultMap.put("handleShopCount", handleShopCount);
        resultMap.put("handleMerchantWithdrawCount", handleMerchantWithdrawCount);
        resultMap.put("handleShopChangeCount", handleShopChangeCount);
        resultMap.put("handleOrderRefundCount", handleOrderRefundCount);
        resultMap.put("underShelfShopCount", underShelfShopCount);
        resultMap.put("onShelfShopCount", onShelfShopCount);
        resultMap.put("allShopCount", allShopCount);
        resultMap.put("dayMemberCount", dayMemberCount);
        resultMap.put("yesterdayMemberCount", yesterdayMemberCount);
        resultMap.put("thisMonthMemberCount", thisMonthMemberCount);
        resultMap.put("allMemberCount", allMemberCount);
        resultMap.put("totalActualPrice", totalActualPrice);
        resultMap.put("totalDeliveryFee", totalDeliveryFee);
        resultMap.put("totalRefundAmount", totalRefundAmount);
        resultMap.put("totalRefundDeliveryFee", totalRefundDeliveryFee);
        resultMap.put("unCompletedNum", unCompletedNum);
        resultMap.put("waitHandleRefundNum", waitHandleRefundNum);
        resultMap.put("balance", balance);
        resultMap.put("daySumIncome", daySumIncome);
        resultMap.put("daySumExpense", daySumExpense);

//        //今日结束时间
//        Calendar today_endCalendar = Calendar.getInstance();
//        today_endCalendar.setTime(new Date());
//        today_endCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        today_endCalendar.set(Calendar.MINUTE, 59);
//        today_endCalendar.set(Calendar.SECOND, 59);
//        today_endCalendar.set(Calendar.MILLISECOND, 999);
//        Date today_endTime = today_endCalendar.getTime();
//
//        //昨日结束时间
//        Calendar yesterday_endCalendar = Calendar.getInstance();
//        yesterday_endCalendar.setTime(DateUtils.subtractDays(new Date(), 1));
//        yesterday_endCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        yesterday_endCalendar.set(Calendar.MINUTE, 59);
//        yesterday_endCalendar.set(Calendar.SECOND, 59);
//        yesterday_endCalendar.set(Calendar.MILLISECOND, 999);
//        Date yesterday_endTime = yesterday_endCalendar.getTime();
//
//        //系统开始使用时间
//        Calendar system_startCalendar = Calendar.getInstance();
//        system_startCalendar.setTime(new Date("1970/1/1 00:00:00"));
//        Date system_startTime = system_startCalendar.getTime();
//
//        //订单表筛选条件-所有商家
//        Order order = new Order();
//
//        //今日支付金额(商家实际到手金额)
//        BigDecimal todaySumMerchantIncome = orderFeignApi.selectSumMerchantIncome(order, system_startTime, today_endTime);
//        //昨日支付金额(商家实际到手金额)
//        BigDecimal yesterdaySumMerchantIncome = orderFeignApi.selectSumMerchantIncome(order, system_startTime, yesterday_endTime);
//
//        //今日支付订单数量
//        int todayCountPaid = orderFeignApi.selectCountCompleted(order, system_startTime, today_endTime);
//        //昨日支付订单数量
//        int yesterdayCountPaid = orderFeignApi.selectCountCompleted(order, system_startTime, yesterday_endTime);
//
//        //今日截止总共普通注册人数
//        int todayCountGeneralRegister = memberFeignApi.selectCountGeneralRegister(system_startTime, today_endTime);
//        //昨日截止总共普通注册人数
//        int yesterdayCountGeneralRegister = memberFeignApi.selectCountGeneralRegister(system_startTime, yesterday_endTime);
//
//        //今日截止总共邀请注册人数
//        int todayCountInviteRegister = memberFeignApi.selectCountInviteRegister(system_startTime, today_endTime);
//        //昨日截止总共邀请注册人数
//        int yesterdayCountInviteRegister = memberFeignApi.selectCountInviteRegister(system_startTime, yesterday_endTime);
//
//        resultMap.put("todaySumMerchantIncome", todaySumMerchantIncome);
//        resultMap.put("yesterdaySumMerchantIncome", yesterdaySumMerchantIncome);
//        resultMap.put("todayCountPaid", todayCountPaid);
//        resultMap.put("yesterdayCountPaid", yesterdayCountPaid);
//        resultMap.put("todayCountGeneralRegister", todayCountGeneralRegister);
//        resultMap.put("yesterdayCountGeneralRegister", yesterdayCountGeneralRegister);
//        resultMap.put("todayCountInviteRegister", todayCountInviteRegister);
//        resultMap.put("yesterdayCountInviteRegister", yesterdayCountInviteRegister);

        return resultMap;
    }

    @Override
    public Map todayStatisticByMerchant(StatisticsParam param) {
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());
        Map resultMap = new HashMap();

        //订单表筛选条件-当前登录商家
        OrderParam order = new OrderParam();
        order.setShopId(loginMerchant.getShopId());
        order.setStartTime(DateUtilsExtend.getDayBegin());
        order.setEndTime(DateUtilsExtend.getDayEnd());
        //今日支付订单、今日实际收入、今日进店人数、今日加购商品
        log.debug("\n\n》》》 StatisticsServiceImpl.todayStatisticByMerchant - order = " + JSON.toJSONString(order));
        int dayCountPaid = orderFeignApi.selectCountCompleted(order).getData();
        BigDecimal daySumMerchantIncome = orderFeignApi.selectSumMerchantIncome(order).getData();
        int todayCountIntoShop = systemUsageRecordFeignApi.selectCountIntoShop(loginMerchant.getShopId(), DateUtilsExtend.getDayBegin(), DateUtilsExtend.getDayEnd()).getData();
        int todayCountShoppingCartGoodsNumber = shoppingCartService.selectCountGoodsNumber(loginMerchant.getShopId(), DateUtilsExtend.getDayBegin(), DateUtilsExtend.getDayEnd());

        //待制作订单(自取)、待配送订单(外卖)、已完成订单、待处理退款申请
        order.setShopId(loginMerchant.getShopId());
        order.setStatus(Quantity.INT_2);
        int waitHandleOrderCount = orderFeignApi.countByExample(order).getData();

        order.setShopId(loginMerchant.getShopId());
        order.setStatus(Quantity.INT_4);
        int waitDeliverOrderCount = orderFeignApi.countByExample(order).getData();

        order.setShopId(loginMerchant.getShopId());
        order.setStatus(Quantity.INT_6);
        int completedOrderCount = orderFeignApi.countByExample(order).getData();

        order.setShopId(loginMerchant.getShopId());
        order.setStatus(Quantity.INT_7);
        int handleOrderRefundCount = orderFeignApi.countByExample(order).getData();

        //商品总览：已下架、已上架、库存售罄、全部商品
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getShopId, loginMerchant.getShopId());
        wrapper.eq(Goods::getStatus, Quantity.INT_3);
        int underShelfGoodsCount = goodsService.count(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getShopId, loginMerchant.getShopId());
        wrapper.eq(Goods::getStatus, Quantity.INT_2);
        int onShelfGoodsCount = goodsService.count(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getShopId, loginMerchant.getShopId());
        wrapper.eq(Goods::getStatus, Quantity.INT_4);
        int sellOutGoodsCount = goodsService.count(wrapper);

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getShopId, loginMerchant.getShopId());
        int allGoodsCount = goodsService.count(wrapper);

        //指数总览：客单价(成交金额/成交订单数)、下单转化率(下单人数/访问人数)、下单-支付转化率(支付人数/下单人数)、支付转化率(支付人数/访问人数)
        Date startDate = new Date("1970/1/1 00:00:00");
        BigDecimal perCustomerTransaction;
        order.setStartTime(startDate);
        order.setEndTime(DateUtilsExtend.getDayEnd());
        BigDecimal sumActualPrice = orderFeignApi.selectSumActualPrice(order).getData();
        int countPaid = orderFeignApi.selectCountCompleted(order).getData();
        if(countPaid == 0){
            perCustomerTransaction = BigDecimal.valueOf(-1);
        }else{
            perCustomerTransaction = sumActualPrice.divide(BigDecimal.valueOf(countPaid), 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal orderConversionRate;
        order.setStartTime(startDate);
        order.setEndTime(DateUtilsExtend.getDayEnd());
        int countOrderPeoples = orderFeignApi.selectCountOrderPeoples(order).getData();
        int countIntoShop = systemUsageRecordFeignApi.selectCountIntoShop(loginMerchant.getShopId(), startDate, DateUtilsExtend.getDayEnd()).getData();
        if(countIntoShop == 0){
            orderConversionRate = BigDecimal.valueOf(-1);
        }else{
            orderConversionRate = BigDecimal.valueOf(countOrderPeoples).divide(BigDecimal.valueOf(countIntoShop), 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal orderPaymentConversionRate;
        order.setStartTime(startDate);
        order.setEndTime(DateUtilsExtend.getDayEnd());
        int countPayers = orderFeignApi.selectCountPayers(order).getData();
        if(countOrderPeoples == 0){
            orderPaymentConversionRate = BigDecimal.valueOf(-1);
        }else{
            orderPaymentConversionRate = BigDecimal.valueOf(countPayers).divide(BigDecimal.valueOf(countOrderPeoples), 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal paymentConversionRate;
        if(countIntoShop == 0){
            paymentConversionRate = BigDecimal.valueOf(-1);
        }else{
            paymentConversionRate = BigDecimal.valueOf(countPayers).divide(BigDecimal.valueOf(countIntoShop), 2, BigDecimal.ROUND_HALF_UP);
        }

        //对百分比做限制，必须控制在0~1之间
        if(orderConversionRate.compareTo(BigDecimal.ONE) > 0){
            orderConversionRate = BigDecimal.ONE;
        }else if(orderConversionRate.compareTo(BigDecimal.ZERO) < 0){
            orderConversionRate = BigDecimal.ZERO;
        }
        if(orderPaymentConversionRate.compareTo(BigDecimal.ONE) > 0){
            orderPaymentConversionRate = BigDecimal.ONE;
        }else if(orderPaymentConversionRate.compareTo(BigDecimal.ZERO) < 0){
            orderPaymentConversionRate = BigDecimal.ZERO;
        }
        if(paymentConversionRate.compareTo(BigDecimal.ONE) > 0){
            paymentConversionRate = BigDecimal.ONE;
        }else if(paymentConversionRate.compareTo(BigDecimal.ZERO) < 0){
            paymentConversionRate = BigDecimal.ZERO;
        }

        resultMap.put("dayCountPaid", dayCountPaid);
        resultMap.put("daySumMerchantIncome", daySumMerchantIncome);
        resultMap.put("todayCountIntoShop", todayCountIntoShop);
        resultMap.put("todayCountShoppingCartGoodsNumber", todayCountShoppingCartGoodsNumber);
        resultMap.put("waitHandleOrderCount", waitHandleOrderCount);
        resultMap.put("waitDeliverOrderCount", waitDeliverOrderCount);
        resultMap.put("completedOrderCount", completedOrderCount);
        resultMap.put("handleOrderRefundCount", handleOrderRefundCount);
        resultMap.put("underShelfGoodsCount", underShelfGoodsCount);
        resultMap.put("onShelfGoodsCount", onShelfGoodsCount);
        resultMap.put("sellOutGoodsCount", sellOutGoodsCount);
        resultMap.put("allGoodsCount", allGoodsCount);
        resultMap.put("perCustomerTransaction", perCustomerTransaction);
        resultMap.put("orderConversionRate", orderConversionRate);
        resultMap.put("orderPaymentConversionRate", orderPaymentConversionRate);
        resultMap.put("paymentConversionRate", paymentConversionRate);

        //今日开始时间
        Calendar today_startCalendar = Calendar.getInstance();
        today_startCalendar.setTime(new Date());
        today_startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        today_startCalendar.set(Calendar.MINUTE, 0);
        today_startCalendar.set(Calendar.SECOND, 0);
        today_startCalendar.set(Calendar.MILLISECOND, 0);
        Date today_startTime = today_startCalendar.getTime();
        //今日结束时间
        Calendar today_endCalendar = Calendar.getInstance();
        today_endCalendar.setTime(new Date());
        today_endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        today_endCalendar.set(Calendar.MINUTE, 59);
        today_endCalendar.set(Calendar.SECOND, 59);
        today_endCalendar.set(Calendar.MILLISECOND, 999);
        Date today_endTime = today_endCalendar.getTime();

        //昨日结束时间
        Calendar yesterday_endCalendar = Calendar.getInstance();
        yesterday_endCalendar.setTime(DateUtilsPlus.subtractDays(new Date(), 1));
        yesterday_endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        yesterday_endCalendar.set(Calendar.MINUTE, 59);
        yesterday_endCalendar.set(Calendar.SECOND, 59);
        yesterday_endCalendar.set(Calendar.MILLISECOND, 999);
        Date yesterday_endTime = yesterday_endCalendar.getTime();
        //昨日开始时间
        Calendar yesterday_startCalendar = Calendar.getInstance();
        yesterday_startCalendar.setTime(DateUtilsPlus.subtractDays(yesterday_endTime, 1));
        yesterday_startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterday_startCalendar.set(Calendar.MINUTE, 0);
        yesterday_startCalendar.set(Calendar.SECOND, 0);
        yesterday_startCalendar.set(Calendar.MILLISECOND, 0);
        Date yesterday_startTime = yesterday_startCalendar.getTime();

        //订单表筛选条件-当前登录商家
        order = new OrderParam();
        order.setShopId(loginMerchant.getShopId());

        //今日进店人数
        todayCountIntoShop = systemUsageRecordFeignApi.selectCountIntoShop(loginMerchant.getShopId(), today_startTime, today_endTime).getData();
        //昨日进店人数
        int yesterdayCountIntoShop = systemUsageRecordFeignApi.selectCountIntoShop(loginMerchant.getShopId(), yesterday_startTime, yesterday_endTime).getData();

        //今日支付金额(商家实际到手金额)
        order.setStartTime(today_startTime);
        order.setEndTime(today_endTime);
        BigDecimal todaySumMerchantIncome = orderFeignApi.selectSumMerchantIncome(order).getData();
        //昨日支付金额(商家实际到手金额)
        order.setStartTime(yesterday_startTime);
        order.setEndTime(yesterday_endTime);
        BigDecimal yesterdaySumMerchantIncome = orderFeignApi.selectSumMerchantIncome(order).getData();

        //今日支付订单数量
        order.setStartTime(today_startTime);
        order.setEndTime(today_endTime);
        int todayCountPaid = orderFeignApi.selectCountCompleted(order).getData();
        //昨日支付订单数量
        order.setStartTime(yesterday_startTime);
        order.setEndTime(yesterday_endTime);
        int yesterdayCountPaid = orderFeignApi.selectCountCompleted(order).getData();

        //今日加购商品数量
        int todayCountGoodsNumber = shoppingCartService.selectCountGoodsNumber(loginMerchant.getShopId(), today_startTime, today_endTime);
        //昨日加购商品数量
        int yesterdayCountGoodsNumber = shoppingCartService.selectCountGoodsNumber(loginMerchant.getShopId(), yesterday_startTime, yesterday_endTime);

        resultMap.put("todayCountIntoShop", todayCountIntoShop);
        resultMap.put("yesterdayCountIntoShop", yesterdayCountIntoShop);
        resultMap.put("todaySumMerchantIncome", todaySumMerchantIncome);
        resultMap.put("yesterdaySumMerchantIncome", yesterdaySumMerchantIncome);
        resultMap.put("todayCountPaid", todayCountPaid);
        resultMap.put("yesterdayCountPaid", yesterdayCountPaid);
        resultMap.put("todayCountGoodsNumber", todayCountGoodsNumber);
        resultMap.put("yesterdayCountGoodsNumber", yesterdayCountGoodsNumber);

        return resultMap;
    }
}