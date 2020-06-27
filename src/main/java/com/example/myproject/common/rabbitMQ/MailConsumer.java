package com.example.myproject.common.rabbitMQ;

import com.example.myproject.common.rabbitMQ.config.MailRabbitConfig;
import com.example.myproject.common.utils.MailUtil;
import com.example.myproject.common.utils.MessageUtil;
import com.example.myproject.common.pojo.Mail;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-05 19:31
 **/
@Slf4j
@Component
public class MailConsumer {
    @Autowired
    private MailUtil mailUtil;

    @RabbitListener(queues = MailRabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        Mail mail = MessageUtil.msgToObj(message, Mail.class);
        log.info("收到消息: {}", mail.toString());
        String msgId = mail.getMsgId();

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        boolean success = mailUtil.sendMail(mail);
        if (success) {
            channel.basicAck(tag, false);// 消费确认
        } else {
            channel.basicNack(tag, false, true);
        }
    }
}
