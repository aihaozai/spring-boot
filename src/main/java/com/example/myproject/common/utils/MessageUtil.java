package com.example.myproject.common.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-05 19:37
 **/
public class MessageUtil {
    public static Message objToMsg(Object obj) {
        if (null == obj) return null;

        Message message = MessageBuilder.withBody(TransformUtil.objToStr(obj).getBytes()).build();
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);// 消息持久化
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

        return message;
    }

    public static <T> T msgToObj(Message message, Class<T> clazz) {
        if (null == message || null == clazz) return null;

        String str = new String(message.getBody());
        T obj = TransformUtil.strToObj(str, clazz);

        return obj;
    }
}
