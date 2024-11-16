package com.agufish.javatest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class MultiConsumer {
    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        for (int i = 0; i < 2; i++) {
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 设置预取计数为 1，这样RabbitMQ就会在给消费者新消息之前等待先前的消息被确认
            channel.basicQos(1);

            int finalI = i;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                try {
                    System.out.println(" [x] Received '" + "编号:" + finalI + ":" + message + "'");
                    Thread.sleep(finalI * 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                    // basicAck代表是在回调函数里的手动确认
                    // channel.basicNack();
                    // channel.basicReject();
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                }
            };
            // autoAck为false代表取消自动确认，使用回调里面手动确认
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        }

    }
}
