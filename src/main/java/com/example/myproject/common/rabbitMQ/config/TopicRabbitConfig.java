package com.example.myproject.common.rabbitMQ.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-06 15:23
 **/
@Configuration
public class TopicRabbitConfig {
    //绑定键
    public final static String man = "topic.man";
    public final static String woman = "topic.woman";

    @Bean
    public Queue firstQueue() {
        // 将普通队列绑定到死信队列交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(DeadLetterConfig.DEAD_LETTER_QUEUE_KEY, DeadLetterConfig.deadExchangeName);
        args.put(DeadLetterConfig.DEAD_LETTER_ROUTING_KEY, DeadLetterConfig.deadRoutingKey);
        return new Queue(man, true, false, false, args);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(woman,true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange",true, false);
    }


    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(firstQueue()).to(exchange()).with(man);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingExchangeMessage2() {
        return BindingBuilder.bind(secondQueue()).to(exchange()).with("topic.#");
    }
}
