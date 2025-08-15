package com.siam.package_order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.example.OrderExample;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.model.vo.OrderVo;
import com.siam.package_order.model.vo.OrderVo2;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {

    void delete(OrderParam param);

    Order insert(OrderParam param);

    /**
     * [本地事务]
     *
     * @param param
     * @param transId
     * @return
     * @author 暹罗
     */
    Order insertByMQ(OrderParam param, String transId) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;

    void cancelOrderByUnPaid(OrderParam param);

    void cancelOrderNoReason(OrderParam param) throws IOException;

    void applyRefund(OrderParam param) throws IOException;

    void confirmReceipt(OrderParam param);

    Order selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Order record);

    Page<Order> getListByPageWithAsc(OrderParam param);

    Page<Map<String, Object>> getListByPageWithDesc(OrderParam param);

    Order selectByOrderNo(String orderNo);

    /**
     * 关闭超时未支付的订单
     */
    void closeOverdueOrder(Integer id);

    void autoCompletedOrder(Integer id);

    void printRceceipts(Integer id);

    int batchUpdateIsPrintedTrue(List<Integer> idList);

    Integer getNextQueueNo();

    /**
     * 今日订单列表
     * @param pageNo
     * @param pageSize
     * @param orderDto
     */
    Page<Order> getListByTodayOrderWithAsc(OrderParam param);

    /**
     * 普通订单回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param outTradeNo 商户单号
     */
    void paymentNotify(String outTradeNo) throws IOException, InterruptedException, RemotingException, MQClientException, MQBrokerException;

    /**
     * 自取订单改为配送回调
     * 支付成功回调时修改订单状态，并且触发一系列关联操作
     * @param changeToDeliveryOutTradeNo 自取订单改为配送的商户单号
     */
    void paymentNotifyOfChangeToDelivery(String changeToDeliveryOutTradeNo);

    /**
     * @description:订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)
     * @throws
     * @author 暹罗
     * @date 2020/4/22 18:47
     */
    Map countOrder(OrderParam order);

    /**
     * 按店铺统计近一月销量/某个时间段的销量
     *
     * @param startTime
     * @param endTime
     * @param shopId
     * @return
     * @author 暹罗
     */
    int selectLatelyMonthlySalesByShopId(Date startTime, Date endTime, Integer shopId);

    /**
     * 查询支付金额(商家实际到手金额)
     */
    BigDecimal selectSumMerchantIncome(OrderParam param);

    /**
     * 查询用户实际支付金额
     */
    BigDecimal selectSumActualPrice(OrderParam param);

    /**
     * 查询支付订单数量
     */
    int selectCountCompleted(OrderParam param);

    /**
     * 查询支付订单数量
     */
    int selectCountPaid(OrderParam param);

    boolean getIsAllowCancelNoReason(OrderParam order);

    boolean getIsAllowApplyRefund(OrderParam order);

    Page<Order> getAfterSalesListByPageWithAsc(OrderParam param);

    /**
     * 检测给商家发放用户下单资金
     * @return
     **/
    void updatePayOrderFrozenBalanceOfMerchant();

    void remindOvertimeOrder(Integer id) throws IOException;

    List<Map<String, Object>> selectStatisticOrder(Integer shopId);

    String selectStartDateOrder(Integer shopId);

    int selectCountPayers(OrderParam param);

    int selectCountOrderPeoples(OrderParam param);

    void refundMerchantBalance(String out_trade_no);

    Map<String, Object> selectSumField(OrderParam order);

    int selectCountUnCompleted(OrderParam order);

    int selectCountWaitHandle(OrderParam order);

    OrderVo selectById(OrderParam param);

    OrderVo2 selectRefundProcess(OrderParam param);

    Map selectTabNumber(OrderParam param);

    void batchUpdateIsPrintedTrue(OrderParam param);

    Map selectAllTabWaitHandleNum(OrderParam param);

    List<Order> waitPrintOrderList(OrderParam param);

    void auditAfterSalesOrderByAdmin(OrderParam param);

    void auditAfterSalesOrderByMerchant(OrderParam param);

    Map statistic(OrderParam param) throws ParseException;

    /**
     *
     * @param dbOrder 订单数据
     * @param printType 打印类型
     * @return
     */
    void printOrderReceipt(Order dbOrder, String printType);

    /**
     * 订单完成后，发放资金给商家、骑手
     * @param dbOrder
     */
    void allocatingFunds(Order dbOrder);
}