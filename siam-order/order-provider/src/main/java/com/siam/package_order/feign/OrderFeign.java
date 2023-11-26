package com.siam.package_order.feign;

import com.alibaba.fastjson.JSON;
import com.siam.package_order.model.example.OrderExample;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/orderFeign")
public class OrderFeign {

    @Autowired
    private OrderService orderService;

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    @RequestMapping(value = "/countOrder")
    public Map countOrder(@RequestBody OrderParam order){
        return orderService.countOrder(order);
    }

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
    public int selectLatelyMonthlySalesByShopId(@RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime, @RequestParam("shopId") Integer shopId){
        return orderService.selectLatelyMonthlySalesByShopId(startTime, endTime, shopId);
    }

    /**
     * 查询支付金额(商家实际到手金额)
     */
    @RequestMapping(value = "/selectSumMerchantIncome")
    public BigDecimal selectSumMerchantIncome(@RequestBody OrderParam param){
        return orderService.selectSumMerchantIncome(param);
    }

    /**
     * 查询用户实际支付金额
     */
    @RequestMapping(value = "/selectSumActualPrice")
    public BigDecimal selectSumActualPrice(@RequestBody OrderParam param){
        return orderService.selectSumActualPrice(param);
    }

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountCompleted")
    public int selectCountCompleted(@RequestBody OrderParam param){
        log.debug("\n\n》》》 OrderFeign.selectCountCompleted - order = " + JSON.toJSONString(param));
        return orderService.selectCountCompleted(param);
    }

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountPaid")
    public int selectCountPaid(@RequestBody OrderParam order){
        return orderService.selectCountPaid(order);
    }

    @RequestMapping(value = "/selectCountUnCompleted")
    public int selectCountUnCompleted(@RequestBody OrderParam order){
        return orderService.selectCountUnCompleted(order);
    }

    @RequestMapping(value = "/selectCountWaitHandle")
    public int selectCountWaitHandle(@RequestBody OrderParam order){
        return orderService.selectCountWaitHandle(order);
    }

    @RequestMapping(value = "/selectSumField")
    public Map<String, Object> selectSumField(@RequestBody OrderParam order){
        return orderService.selectSumField(order);
    }

    @RequestMapping(value = "/countByExample")
    public int countByExample(@RequestBody OrderParam order){
        OrderExample example = new OrderExample();
        OrderExample.Criteria criteria = example.createCriteria();
        if(order.getShopId() != null){
            criteria.andShopIdEqualTo(order.getShopId());
        }
        if(order.getStatus() != null){
            criteria.andStatusEqualTo(order.getStatus());
        }
        return orderService.countByExample(example);
    }

    @RequestMapping(value = "/selectCountOrderPeoples")
    public int selectCountOrderPeoples(@RequestBody OrderParam param){
        return orderService.selectCountOrderPeoples(param);
    }

    @RequestMapping(value = "/selectCountPayers")
    public int selectCountPayers(@RequestBody OrderParam param){
        return orderService.selectCountPayers(param);
    }
}