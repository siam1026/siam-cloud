package com.siam.package_order.feign;

import com.alibaba.fastjson.JSON;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.example.OrderExample;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RestController
public class OrderFeignProvider implements OrderFeignApi {

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    public BasicResult countOrder(OrderParam order){
        return BasicResult.success(orderService.countOrder(order));
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
    public BasicResult selectLatelyMonthlySalesByShopId(Date startTime, Date endTime, Integer shopId){
        return BasicResult.success(orderService.selectLatelyMonthlySalesByShopId(startTime, endTime, shopId));
    }

    /**
     * 查询支付金额(商家实际到手金额)
     */
    public BasicResult selectSumMerchantIncome(OrderParam param){
        return BasicResult.success(orderService.selectSumMerchantIncome(param));
    }

    /**
     * 查询用户实际支付金额
     */
    public BasicResult selectSumActualPrice(OrderParam param){
        return BasicResult.success(orderService.selectSumActualPrice(param));
    }

    /**
     * 查询支付订单数量
     */
    public BasicResult selectCountCompleted(OrderParam param){
        log.debug("\n\n》》》 OrderFeign.selectCountCompleted - order = " + JSON.toJSONString(param));
        return BasicResult.success(orderService.selectCountCompleted(param));
    }

    /**
     * 查询支付订单数量
     */
    public BasicResult selectCountPaid(OrderParam order){
        return BasicResult.success(orderService.selectCountPaid(order));
    }

    public BasicResult selectCountUnCompleted(OrderParam order){
        return BasicResult.success(orderService.selectCountUnCompleted(order));
    }

    public BasicResult selectCountWaitHandle(OrderParam order){
        return BasicResult.success(orderService.selectCountWaitHandle(order));
    }

    public BasicResult selectSumField(OrderParam order){
        return BasicResult.success(orderService.selectSumField(order));
    }

    public BasicResult countByExample(OrderParam order){
        OrderExample example = new OrderExample();
        OrderExample.Criteria criteria = example.createCriteria();
        if(order.getShopId() != null){
            criteria.andShopIdEqualTo(order.getShopId());
        }
        if(order.getStatus() != null){
            criteria.andStatusEqualTo(order.getStatus());
        }
        if(order.getCreateTimeGreaterThan() != null){
            criteria.andCreateTimeGreaterThan(order.getCreateTimeGreaterThan());
        }
        if(order.getExcludeStatusList() != null){
            criteria.andStatusNotIn(order.getExcludeStatusList());
        }
        return BasicResult.success(orderService.countByExample(example));
    }

    public BasicResult selectCountOrderPeoples(OrderParam param){
        return BasicResult.success(orderService.selectCountOrderPeoples(param));
    }

    @Override
    public BasicResult updatePayOrderFrozenBalanceOfMerchant() {
        orderService.updatePayOrderFrozenBalanceOfMerchant();
        return BasicResult.success();
    }

    public BasicResult selectCountPayers(OrderParam param){
        return BasicResult.success(orderService.selectCountPayers(param));
    }
}