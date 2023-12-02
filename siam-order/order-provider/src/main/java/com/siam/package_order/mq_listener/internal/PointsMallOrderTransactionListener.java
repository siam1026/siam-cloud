package com.siam.package_order.mq_listener.internal;

import com.alibaba.fastjson.JSONObject;
import com.siam.package_order.entity.TransactionLog;
import com.siam.package_order.model.param.internal.PointsMallOrderParam;
import com.siam.package_order.service.internal.PointsMallOrderService;
import com.siam.package_order.service_impl.TransactionLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PointsMallOrderTransactionListener implements TransactionListener {

    @Resource(name = "rocketMQ_PointsMallOrderServiceImpl")
    private PointsMallOrderService orderService;

    @Autowired
    private TransactionLogService transactionLogService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("[订单服务] 开始执行本地事务....");
        LocalTransactionState state;
        //执行本地事务
        try {
            PointsMallOrderParam param = JSONObject.parseObject(new String(message.getBody()), PointsMallOrderParam.class);
            orderService.insertByMQ(param, message.getTransactionId());
            state = LocalTransactionState.COMMIT_MESSAGE;
            log.info("[订单服务] 本地事务已提交。{}", message.getTransactionId());
        } catch (Exception e) {
            log.error("[订单服务] 执行本地事务失败。{}", e.getMessage());
            state = LocalTransactionState.ROLLBACK_MESSAGE;
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("[订单服务] 开始回查本地事务....");
        LocalTransactionState state;
        //如果本地事务存在，则事务提交成功
        TransactionLog transactionLog = transactionLogService.getById(messageExt.getTransactionId());
        if(transactionLog != null){
            state = LocalTransactionState.COMMIT_MESSAGE;
        }else{
            state = LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return state;
    }
}