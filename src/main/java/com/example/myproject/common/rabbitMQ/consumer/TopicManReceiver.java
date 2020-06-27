package com.example.myproject.common.rabbitMQ.consumer;

import com.rabbitmq.client.Channel;
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
 * @Create: 2019-11-06 16:03
 **/
@Component
@RabbitListener(queues = "topic.man",containerFactory = "rabbitListenerContainerFactory")
public class TopicManReceiver {
    private int resend = 0;
    @RabbitHandler
    public void consume(@Payload String message ,@Header(AmqpHeaders.DELIVERY_TAG)long deliveryTag,Channel channel) throws IOException, InterruptedException {
        System.out.println("TopicManReceiver消费者收到消息  : " + message);
        try {
            int num = 1/0;
            resend = 0;
            channel.basicAck(deliveryTag, false);// 手动消费确认
        }catch (Exception e){
            e.printStackTrace();
            if(resend<3) {  //连续同一消息消费失败
                resend++;
                Thread.sleep(1000);
                channel.basicNack(deliveryTag, false, true);    //消息重新发送
            }else {
                //拒绝消费消息（丢失消息） 给死信队列
                channel.basicNack(deliveryTag, false, false);
            }
        }

    }
}
