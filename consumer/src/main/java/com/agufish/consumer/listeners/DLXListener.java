package com.agufish.consumer.listeners;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class DLXListener {

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


    @RabbitListener(queues = {"delay_queue"}, ackMode = "MANUAL")
    // @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag是一个方法参数注解,用于从消息头中获取投递标签(deliveryTag),
    // 在RabbitMQ中,每条消息都会被分配一个唯一的投递标签，用于标识该消息在通道中的投递状态和顺序。通过使用@Header(AmqpHeaders.DELIVERY_TAG)注解,可以从消息头中提取出该投递标签,并将其赋值给long deliveryTag参数。
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        // 使用日志记录器打印接收到的消息内容
        log.info("delay_queue receiveMessage message = {}", message);
        System.out.println("队列的消费确认机制：" + getAcknowledgeModeForQueue("delay_queue"));

        // 投递标签是一个数字标识,它在消息消费者接收到消息后用于向RabbitMQ确认消息的处理状态。通过将投递标签传递给channel.basicAck(deliveryTag, false)方法,可以告知RabbitMQ该消息已经成功处理,可以进行确认和从队列中删除。
        // 手动确认消息的接收，向RabbitMQ发送确认消息
        channel.basicNack(deliveryTag, false, false);
    }

    @RabbitListener(queues = "dlx_queue")
    public void manualListener(String message) {
        log.info("手动操作的dlx_queue receiveMessage message = {}", message);
        System.out.println("手动操作中死信队列的消费确认机制：" + getAcknowledgeModeForQueue("dlx_queue"));
    }


}



