package com.example.myproject.common.rabbitMQ.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-11 11:33
 **/
@Configuration
public class DeadLetterConfig {
    /**
     * 定义死信队列相关信息
     */
    public final static String deadQueueName = "dead_queue";
    public final static String deadRoutingKey = "dead_routing_key";
    public final static String deadExchangeName = "dead_exchange";
    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 创建配置死信邮件队列
     * @return queue
     */
    @Bean
    public Queue deadQueue() {
        return new Queue(deadQueueName, true);
    }
    /*
     * 创建死信交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadExchangeName);
    }
    /*
     * 死信队列与死信交换机绑定
     */
    @Bean
    public Binding bindingDeadExchange(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(deadRoutingKey);
    }
}
