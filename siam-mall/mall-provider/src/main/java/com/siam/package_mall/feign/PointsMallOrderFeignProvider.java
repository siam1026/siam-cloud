package com.siam.package_mall.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallOrder;
import com.siam.package_mall.model.example.PointsMallOrderExample;
import com.siam.package_mall.model.param.PointsMallOrderParam;
import com.siam.package_mall.service.PointsMallOrderService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallOrderFeignProvider implements PointsMallOrderFeignApi {

    @Resource(name = "pointsMallOrderServiceImpl")
    private PointsMallOrderService pointsMallOrderService;

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author Chen Qu
     * @date 2020/4/22 18:47
     */
    public BasicResult countOrder(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.countOrder(order));

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
        return BasicResult.success(pointsMallOrderService.selectLatelyMonthlySalesByShopId(startTime, endTime, shopId));
    }

    /**
     * 查询支付金额(商家实际到手金额)
     */
    public BasicResult selectSumMerchantIncome(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectSumMerchantIncome(order, order.getStartTime(), order.getEndTime()));
    }

    /**
     * 查询用户实际支付金额
     */
    public BasicResult selectSumActualPrice(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectSumActualPrice(order, order.getStartTime(), order.getEndTime()));
    }

    /**
     * 查询支付订单数量
     */
    public BasicResult selectCountCompleted(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectCountCompleted(order, order.getStartTime(), order.getEndTime()));
    }

    /**
     * 查询支付订单数量
     */
    public BasicResult selectCountPaid(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectCountPaid(order, order.getStartTime(), order.getEndTime()));
    }

//    public BasicResult selectCountUnCompleted(PointsMallOrderParam order){
//        return BasicResult.success(pointsMallOrderService.selectCountUnCompleted(order));
//    }
//
//    public BasicResult selectCountWaitHandle(PointsMallOrderParam order){
//        return BasicResult.success(pointsMallOrderService.selectCountWaitHandle(order));
//    }

    public BasicResult countByExample(PointsMallOrderParam order){
        PointsMallOrderExample example = new PointsMallOrderExample();
        PointsMallOrderExample.Criteria criteria = example.createCriteria();
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
        return BasicResult.success(pointsMallOrderService.countByExample(example));
    }

    public BasicResult selectCountPayers(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectCountPayers(order, order.getStartTime(), order.getEndTime()));
    }

    public BasicResult selectCountOrderPeoples(PointsMallOrderParam order){
        return BasicResult.success(pointsMallOrderService.selectCountOrderPeoples(order, order.getStartTime(), order.getEndTime()));
    }

    @Override
    public BasicResult syncOrderLogisticsInfo() {
        pointsMallOrderService.syncOrderLogisticsInfo();
        return BasicResult.success();
    }

    @Override
    public BasicResult updateByFinishOverdueOrder() {
        pointsMallOrderService.updateByFinishOverdueOrder();
        return BasicResult.success();
    }

    @Override
    public BasicResult paymentNotify(String outTradeNo) {
        pointsMallOrderService.paymentNotify(outTradeNo);
        return BasicResult.success();
    }

    @Override
    public BasicResult<PointsMallOrder> selectByOrderNo(String orderNo) {
        return BasicResult.success(pointsMallOrderService.selectByOrderNo(orderNo));
    }

    @Override
    public BasicResult updateByPrimaryKeySelective(PointsMallOrder record) {
        pointsMallOrderService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }
}