package com.siam.package_order.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.param.OrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@FeignClient(value = "order-siam")
public interface OrderFeignApi {

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    @PostMapping(value = "/api/order/countOrder")
    BasicResult countOrder(@RequestBody OrderParam order);

    /**
     * 按店铺统计近一月销量/某个时间段的销量
     *
     * @param startTime
     * @param endTime
     * @param shopId
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/api/order/selectLatelyMonthlySalesByShopId")
    BasicResult<Integer> selectLatelyMonthlySalesByShopId(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestParam("shopId") Integer shopId);

    /**
     * 查询支付金额(商家实际到手金额)
     */
    @PostMapping(value = "/api/order/selectSumMerchantIncome")
    BasicResult<BigDecimal> selectSumMerchantIncome(@RequestBody OrderParam order);

    /**
     * 查询用户实际支付金额
     */
    @PostMapping(value = "/api/order/selectSumActualPrice")
    BasicResult<BigDecimal> selectSumActualPrice(@RequestBody OrderParam order);

    /**
     * 查询支付订单数量
     */
    @PostMapping(value = "/api/order/selectCountCompleted")
    BasicResult<Integer> selectCountCompleted(@RequestBody OrderParam order);

    /**
     * 查询支付订单数量
     */
    @PostMapping(value = "/api/order/selectCountPaid")
    BasicResult selectCountPaid(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/selectCountUnCompleted")
    BasicResult<Integer> selectCountUnCompleted(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/selectCountWaitHandle")
    BasicResult<Integer> selectCountWaitHandle(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/selectSumField")
    BasicResult<Map<String, Object>> selectSumField(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/countByExample")
    BasicResult<Integer> countByExample(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/selectCountPayers")
    BasicResult<Integer> selectCountPayers(@RequestBody OrderParam order);

    @PostMapping(value = "/api/order/selectCountOrderPeoples")
    BasicResult<Integer> selectCountOrderPeoples(@RequestBody OrderParam order);

    /**
     * 检测给商家发放用户下单资金
     * @return
     **/
    @PostMapping(value = "/api/order/updatePayOrderFrozenBalanceOfMerchant")
    BasicResult updatePayOrderFrozenBalanceOfMerchant();
}