package com.example.myproject.common.rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-06 16:02
 **/
@Component
public class DeadLetterReceiver {
    @RabbitListener(queues = "dead_queue")
    @RabbitHandler
    public void consume(@Payload String message , @Header(AmqpHeaders.DELIVERY_TAG)long deliveryTag, Channel channel) throws IOException {
        System.out.println("死信消费者收到消息  : " + message);
        channel.basicAck(deliveryTag, false);// 消费确认
    }
}
