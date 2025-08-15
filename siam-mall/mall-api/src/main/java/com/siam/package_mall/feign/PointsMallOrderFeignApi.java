package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallOrder;
import com.siam.package_mall.model.param.PointsMallOrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;

@FeignClient(value = "mall-siam")
public interface PointsMallOrderFeignApi {

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author 暹罗
     * @date 2020/4/22 18:47
     */
    @PostMapping(value = "/api/pointsMallOrder/countOrder")
    BasicResult countOrder(@RequestBody PointsMallOrderParam order);

    /**
     * 按店铺统计近一月销量/某个时间段的销量
     *
     * @param startTime
     * @param endTime
     * @param shopId
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/api/pointsMallOrder/selectLatelyMonthlySalesByShopId")
    BasicResult selectLatelyMonthlySalesByShopId(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestParam("shopId") Integer shopId);

    /**
     * 查询支付金额(商家实际到手金额)
     */
    @PostMapping(value = "/api/pointsMallOrder/selectSumMerchantIncome")
    BasicResult selectSumMerchantIncome(@RequestBody PointsMallOrderParam order);

    /**
     * 查询用户实际支付金额
     */
    @PostMapping(value = "/api/pointsMallOrder/selectSumActualPrice")
    BasicResult<BigDecimal> selectSumActualPrice(@RequestBody PointsMallOrderParam order);

    /**
     * 查询支付订单数量
     */
    @PostMapping(value = "/api/pointsMallOrder/selectCountCompleted")
    BasicResult<Integer> selectCountCompleted(@RequestBody PointsMallOrderParam order);

    /**
     * 查询支付订单数量
     */
    @PostMapping(value = "/api/pointsMallOrder/selectCountPaid")
    BasicResult<Integer> selectCountPaid(@RequestBody PointsMallOrderParam order);

//    @PostMapping(value = "/api/pointsMallOrder/selectCountUnCompleted")
//    BasicResult selectCountUnCompleted(@RequestBody PointsMallOrderParam order);
//
//    @PostMapping(value = "/api/pointsMallOrder/selectCountWaitHandle")
//    BasicResult selectCountWaitHandle(@RequestBody PointsMallOrderParam order);

    @PostMapping(value = "/api/pointsMallOrder/countByExample")
    BasicResult<Integer> countByExample(@RequestBody PointsMallOrderParam param);

    @PostMapping(value = "/api/pointsMallOrder/selectCountPayers")
    BasicResult<Integer> selectCountPayers(@RequestBody PointsMallOrderParam order);

    @PostMapping(value = "/api/pointsMallOrder/selectCountOrderPeoples")
    BasicResult<Integer> selectCountOrderPeoples(@RequestBody PointsMallOrderParam order);

    @PostMapping(value = "/api/pointsMallOrder/syncOrderLogisticsInfo")
    BasicResult syncOrderLogisticsInfo();

    @PostMapping(value = "/api/pointsMallOrder/updateByFinishOverdueOrder")
    BasicResult updateByFinishOverdueOrder();

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    @PostMapping(value = "/api/pointsMallOrder/paymentNotify")
    BasicResult paymentNotify(@RequestParam("outTradeNo") String outTradeNo);

    @PostMapping(value = "/api/pointsMallOrder/selectByOrderNo")
    BasicResult<PointsMallOrder> selectByOrderNo(@RequestParam("orderNo") String orderNo);

    @PostMapping(value = "/api/pointsMallOrder/updateByPrimaryKeySelective")
    BasicResult updateByPrimaryKeySelective(@RequestBody PointsMallOrder record);
}