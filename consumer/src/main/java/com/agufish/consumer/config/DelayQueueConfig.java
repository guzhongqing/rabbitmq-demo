package com.agufish.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


@Configuration
public class DelayQueueConfig {

    private final String dlxExchangeName = "dlx_exchange";
    private final String dlxQueueName = "dlx_queue";
    private final String dlxKey = "dlx_key";

    // 序列化
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // 死信交换机，默认持久化
    @Bean
    public DirectExchange dlxDirectExchange() {
        return new DirectExchange(dlxExchangeName);
    }


    // 死信队列，默认持久化
    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueueName);
    }


    // 绑定死信队列和交换机
    @Bean
    public Binding bindDLX() {
        return BindingBuilder.bind(dlxQueue()).to(dlxDirectExchange()).with(dlxKey);
    }


    // 延迟交换机
    @Bean
    public DirectExchange delayDirectExchange() {
        return new DirectExchange("delay_exchange", true, false);
    }

    // 延迟队列，通过参数指定死信交换机
    @Bean
    public Queue delayQueue() {
        // 设置参数
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlxExchangeName);
        args.put("x-dead-letter-routing-key", dlxKey);
        args.put("x-message-ttl", 10000); // 消息过期时间，单位毫秒
        return new Queue("delay_queue", true, false, false, args);
    }


    // 绑定延迟队列和交换机
    @Bean
    public Binding bindDelay() {
        return BindingBuilder.bind(delayQueue()).to(delayDirectExchange()).with("delay");
    }


}
