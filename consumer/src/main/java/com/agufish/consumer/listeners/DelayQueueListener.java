package com.agufish.consumer.listeners;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DelayQueueListener {

    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public AcknowledgeMode getAcknowledgeModeForQueue(String queueName) {
        for (MessageListenerContainer container : rabbitListenerEndpointRegistry.getListenerContainers()) {
            if (container instanceof SimpleMessageListenerContainer) {
                SimpleMessageListenerContainer simpleContainer = (SimpleMessageListenerContainer) container;
                for (String queue : simpleContainer.getQueueNames()) {
                    if (queue.equals(queueName)) {
                        return simpleContainer.getAcknowledgeMode();
                    }
                }
            }
        }
        return null;
    }


    @RabbitListener(queues = "dlx_queue")
    public void dlxListener(String message) throws InterruptedException {
        log.info("日志");
        System.out.println("dlx queue Received <" + message + ">");
        System.out.println("队列的消费确认机制：" + getAcknowledgeModeForQueue("dlx_queue"));
    }


}
