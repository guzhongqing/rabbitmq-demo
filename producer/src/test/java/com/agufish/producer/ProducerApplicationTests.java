package com.agufish.producer;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class ProducerApplicationTests {
    @Resource
    private YMLConfig ymlConfig;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        System.out.println(ymlConfig.getHost());
        System.out.println(ymlConfig.getPort());
        System.out.println(ymlConfig.getUsername());
        System.out.println(ymlConfig.getPassword());
    }

    @Test
    public void FanoutProducer() {
        String exchangeName = "fanout";
        String message = "hello fanout";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }


    @Test
    public void DirectProducerBlue() {
        String exchangeName = "direct";
        String message = "hello direct blue";
        rabbitTemplate.convertAndSend(exchangeName, "blue", message);
    }

    @Test
    public void DirectProducerYellow() {
        String exchangeName = "direct";
        String message = "hello direct yellow";
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message);
    }

    @Test
    public void DirectProducerRed() {
        String exchangeName = "direct";
        String message = "hello direct red";
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }

    @Test
    public void TopicProducer() {
        String exchangeName = "amq.topic";
        String message1 = "hello topic china.weather";
        String message2 = "hello topic american.news";
        String message3 = "hello topic aaa.china.news";

        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message1);
        rabbitTemplate.convertAndSend(exchangeName, "american.news", message2);
        rabbitTemplate.convertAndSend(exchangeName, "aaa.china.news", message3);
    }


    @Test
    public void objectProducer() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "jack");
        map.put("age", "24");

        String queueName = "object queue";
        rabbitTemplate.convertAndSend(queueName, map);
    }


    @Test
    public void delayProducer() {
        // 这对象转发到死信队列序列化有问题
//        HashMap<String, String> map = new HashMap<>();
//        map.put("name", "jack");
//        map.put("age", "24");
        rabbitTemplate.convertAndSend("delay_exchange", "delay", "从延迟队列发到死信队列的消息");
    }

    @Test
    public void manualProducer() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("delay_exchange", "delay", "从延迟队列发到死信队列的消息");

        }
    }


}


