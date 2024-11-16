package com.agufish.consumer.listeners;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MqListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout queue1", durable = "true"),
            exchange = @Exchange(name = "fanout", type = ExchangeTypes.FANOUT)
    ))
    public void fanoutQueue1Listener(String message) throws InterruptedException {
        System.out.println("fanout queue1 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout queue2", durable = "true"),
            exchange = @Exchange(name = "fanout", type = ExchangeTypes.FANOUT)
    ))
    public void fanoutQueue2Listener(String message) throws InterruptedException {
        System.out.println("fanout queue2 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout queue3", durable = "true"),
            exchange = @Exchange(name = "fanout", type = ExchangeTypes.FANOUT)
    ))
    public void fanoutQueue3Listener(String message) throws InterruptedException {
        System.out.println("fanout queue3 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct queue1", durable = "true"),
            exchange = @Exchange(name = "direct"),
            key = {"blue", "red"}
    ))
    public void directQueue1Listener(String message) throws InterruptedException {
        System.out.println("direct queue1 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct queue2", durable = "true"),
            exchange = @Exchange(name = "direct"),
            key = {"yellow", "red"}
    ))
    public void directQueue2Listener(String message) throws InterruptedException {
        System.out.println("topic queue2 Received <" + message + ">");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic queue1", durable = "true"),
            exchange = @Exchange(name = "amq.topic", type = "topic"),
            key = {"*.weather"}
    ))
    public void topicQueue1Listener(String message) throws InterruptedException {
        System.out.println("topic queue1 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic queue2", durable = "true"),
            exchange = @Exchange(name = "amq.topic", type = "topic"),
            key = {"american.*"}
    ))
    public void topicQueue2Listener(String message) throws InterruptedException {
        System.out.println("topic queue2 Received <" + message + ">");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic queue3", durable = "true"),
            exchange = @Exchange(name = "amq.topic", type = "topic"),
            key = {"#.news"}
    ))
    public void topicQueue3Listener(String message) throws InterruptedException {
        System.out.println("topic queue3 Received <" + message + ">");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "object queue"),
            exchange = @Exchange(name = "amq.direct")
    ))
    public void objectListener(Map<String, Object> obj) throws InterruptedException {
        System.out.println("object queue Received <" + obj + ">");
    }


}
