package com.siam.package_order.feign.internal;

import com.siam.package_order.model.example.internal.PointsMallOrderExample;
import com.siam.package_order.model.param.internal.PointsMallOrderParam;
import com.siam.package_order.service.internal.PointsMallOrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/pointsMallOrderFeign")
public class PointsMallOrderFeign {

    @Resource(name = "pointsMallOrderServiceImpl")
    private PointsMallOrderService pointsMallOrderService;

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    @RequestMapping(value = "/countOrder")
    public Map countOrder(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.countOrder(order);
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
        return pointsMallOrderService.selectLatelyMonthlySalesByShopId(startTime, endTime, shopId);
    }

    /**
     * 查询支付金额(商家实际到手金额)
     */
    @RequestMapping(value = "/selectSumMerchantIncome")
    public BigDecimal selectSumMerchantIncome(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectSumMerchantIncome(order, order.getStartTime(), order.getEndTime());
    }

    /**
     * 查询用户实际支付金额
     */
    @RequestMapping(value = "/selectSumActualPrice")
    public BigDecimal selectSumActualPrice(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectSumActualPrice(order, order.getStartTime(), order.getEndTime());
    }

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountCompleted")
    public int selectCountCompleted(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectCountCompleted(order, order.getStartTime(), order.getEndTime());
    }

    /**
     * 查询支付订单数量
     */
    @RequestMapping(value = "/selectCountPaid")
    public int selectCountPaid(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectCountPaid(order, order.getStartTime(), order.getEndTime());
    }
//
//    @RequestMapping(value = "/selectCountUnCompleted")
//    public int selectCountUnCompleted(@RequestBody PointsMallOrderParam order){
//        return pointsMallOrderService.selectCountUnCompleted(order);
//    }
//
//    @RequestMapping(value = "/selectCountWaitHandle")
//    public int selectCountWaitHandle(@RequestBody PointsMallOrderParam order){
//        return pointsMallOrderService.selectCountWaitHandle(order);
//    }


    @RequestMapping(value = "/countByExample")
    public int countByExample(@RequestBody PointsMallOrderParam order){
        PointsMallOrderExample example = new PointsMallOrderExample();
        PointsMallOrderExample.Criteria criteria = example.createCriteria();
        if(order.getShopId() != null){
            criteria.andShopIdEqualTo(order.getShopId());
        }
        if(order.getStatus() != null){
            criteria.andStatusEqualTo(order.getStatus());
        }
        return pointsMallOrderService.countByExample(example);
    }

    @RequestMapping(value = "/selectCountPayers")
    public int selectCountPayers(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectCountPayers(order, order.getStartTime(), order.getEndTime());
    }

    @RequestMapping(value = "/selectCountOrderPeoples")
    public int selectCountOrderPeoples(@RequestBody PointsMallOrderParam order){
        return pointsMallOrderService.selectCountOrderPeoples(order, order.getStartTime(), order.getEndTime());
    }
}