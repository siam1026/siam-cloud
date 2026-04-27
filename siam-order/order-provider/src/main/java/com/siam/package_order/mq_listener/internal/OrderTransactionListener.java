package com.siam.package_order.mq_listener.internal;

import com.alibaba.fastjson.JSON;
import com.siam.package_common.constant.Quantity;
import com.siam.package_order.entity.Order;
import com.siam.package_order.mapper.OrderMapper;
import com.siam.package_order.model.dto.OrderPaySuccessMessage;
import com.siam.package_order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 支付成功事务消息监听器
 * executeLocalTransaction: 执行下游异步通知链路（公众号消息、语音提醒、打印）
 * checkLocalTransaction: 回查订单支付状态，确认事务是否应提交
 */
@Component
@Slf4j
public class OrderTransactionListener implements TransactionListener {

    @Autowired
    private OrderMapper orderMapper;

    @Lazy
    @Autowired
    private OrderService orderService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("[订单服务] 开始执行支付成功本地事务，transId={}", message.getTransactionId());
        try {
            OrderPaySuccessMessage msg = JSON.parseObject(new String(message.getBody()), OrderPaySuccessMessage.class);
            orderService.executePaymentNotify(msg.getOrderId());
            log.info("[订单服务] 支付成功本地事务执行完成，transId={}", message.getTransactionId());
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            log.error("[订单服务] 支付成功本地事务执行失败，{}", e.getMessage(), e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("[订单服务] 开始回查支付本地事务，transId={}", messageExt.getTransactionId());
        OrderPaySuccessMessage msg = JSON.parseObject(new String(messageExt.getBody()), OrderPaySuccessMessage.class);
        Order dbOrder = orderMapper.selectById(msg.getOrderId());
        if (dbOrder != null && (dbOrder.getStatus() == Quantity.INT_2 || dbOrder.getStatus() == Quantity.INT_4)) {
            log.info("[订单服务] 回查确认：订单已支付，状态={}", dbOrder.getStatus());
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        log.warn("[订单服务] 回查失败：订单未支付或不存在，orderId={}", msg.getOrderId());
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
