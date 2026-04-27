package com.siam.package_common.rabbitmq_config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
public class MsgSendReturnsCallback implements RabbitTemplate.ReturnsCallback {

    /**
     * 当消息从交换机到队列失败时，该方法被调用。
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.debug("\nMsgSendReturnCallback [消息从交换机到队列失败] replyCode:{} replyText:{} exchange:{} routingKey:{} message:{}",
                returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey(), returned.getMessage());
    }
}
