package com.siam.package_order.mq_producer.internal;

import com.siam.package_order.mq_listener.internal.PointsMallOrderTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class PointsMallTransactionProducer {

    private static final String GROUP_NAME = "order_trans_group";

    private TransactionMQProducer producer;

    @Autowired
    private PointsMallOrderTransactionListener pointsMallOrderTransactionListener;

    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @PostConstruct
    public void init(){
        producer = new TransactionMQProducer(GROUP_NAME);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setSendMsgTimeout(100);
        producer.setExecutorService(executor);
        producer.setTransactionListener(pointsMallOrderTransactionListener);
        this.start();
    }

    public void start(){
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送事务消息
     *
     * @param
     * @return
     * @author 暹罗
     */
    public TransactionSendResult send(String topic, String data) throws MQClientException {
        Message message = new Message(topic, data.getBytes());
        return producer.sendMessageInTransaction(message, 10000);
    }
}