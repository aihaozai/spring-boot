package com.example.myproject.common.rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import java.io.IOException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-06 16:02
 **/
@Component
public class TopicTotalReceiver {

    @RabbitListener(queues = "topic.woman",containerFactory = "rabbitListenerContainerFactory")
    public void process(Message message , Channel channel) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        System.out.println("TopicTotalReceiver消费者收到消息  : " + message);
        //channel.basicAck(tag, false);// 消费确认
    }
}
