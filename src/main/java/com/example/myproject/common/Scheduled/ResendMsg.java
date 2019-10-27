package com.example.myproject.common.Scheduled;

import com.example.myproject.common.config.RabbitMQConfig;
import com.example.myproject.common.utils.MessageUtil;
import com.example.myproject.entity.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-05 20:47
 **/
@Slf4j
@Component
public class ResendMsg {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 最大投递次数
    private static final int MAX_TRY_COUNT = 3;

    /**
     * 每30s拉取投递失败的消息, 重新投递
     */
   /* @Scheduled(cron = "0/30 * * * * ?")
    public void resend() {
        log.info("开始执行定时任务(重新投递消息)");
        Mail mail = new Mail();
        mail.setMsgId("ddddddddd");
        mail.setTo("1020146023@qq.com");
        mail.setTitle("测试");
        mail.setContent("内容");
       // rabbitTemplate.convertAndSend(RabbitMQConfig.MAIL_EXCHANGE_NAME, RabbitMQConfig.MAIL_ROUTING_KEY_NAME, MessageUtil.objToMsg(mail), new CorrelationData(mail.getMsgId()));// 重新投递
        log.info("定时任务执行结束(重新投递消息)");
    }*/
}
