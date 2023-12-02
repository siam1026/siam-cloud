package com.siam.package_feign.mod_feign.order.internal;

import com.siam.package_order.entity.internal.PointsMallOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@FeignClient(value = "order-siam", path = "/rest/pointsMallOrderFeign")
public interface PointsMallOrderFeignClient {

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    @RequestMapping(value = "/countOrder")
    Map countOrder(@RequestBody PointsMallOrder order);

    /**
     * 按店铺统计近一月销量/某个时间段的销量
     *
     * @param startTime
     * @param endTime
     * @param shopId
     * @return
     * @author 暹罗
     */
    @RequestMapping(value = "/selectLatelyMonthlySalesByShopId")
    int selectLatelyMonthlySalesByShopId(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestParam("shopId") Integer shopId);

    /**
     * 查询支付金额(商家实际到手金额)
     */
    @RequestMapping(value = "/selectSumMerchantIncome")
    BigDecimal selectSumMerchantIncome(@RequestBody PointsMallOrder order);

    /**
     * 查询用户实际支付金额
     */
    @RequestMapping(value = "/selectSumActualPrice")
    BigDecimal selectSumActualPrice(@RequestBody PointsMallOrder order);

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountCompleted")
    int selectCountCompleted(@RequestBody PointsMallOrder order);

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountPaid")
    int selectCountPaid(@RequestBody PointsMallOrder order);

    @RequestMapping(value = "/selectCountUnCompleted")
    int selectCountUnCompleted(@RequestBody PointsMallOrder order);

    @RequestMapping(value = "/selectCountWaitHandle")
    int selectCountWaitHandle(@RequestBody PointsMallOrder order);

    @RequestMapping(value = "/countByExample")
    int countByExample(@RequestBody PointsMallOrder example);

    @RequestMapping(value = "/selectCountPayers")
    int selectCountPayers(@RequestBody PointsMallOrder order);

    @RequestMapping(value = "/selectCountOrderPeoples")
    int selectCountOrderPeoples(@RequestBody PointsMallOrder order);
}