package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * 简单示例
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/2
 */
@Slf4j
public class SimpleDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://futao:123456789@localhost:5672");
        connectionFactory.setVirtualHost("/tech-sharing");
        // 创建TCP连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义交换机
        channel.exchangeDeclare("simple-exchange", BuiltinExchangeType.TOPIC, true, false, false, null);
        // 定义队列
        channel.queueDeclare("simple-queue", true, false, false, null);
        // 绑定
        channel.queueBind("simple-queue", "simple-exchange", "simple.routing.key.#");

        // 为了不阻塞主线程
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
                try {
                    // 发送消息
                    channel.basicPublish("simple-exchange",
                            "simple.routing.key." + i, true, false,
                            basicProperties, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
                    log.info("发送消息:[{}]", i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "producer").start();

        // 创建2个消费者
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    // 创建消费者通道
                    Channel consumerChannel = connection.createChannel();
                    // 每次缓存的消息数量
                    consumerChannel.basicQos(1);
                    DefaultConsumer consumer = new DefaultConsumer(consumerChannel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            log.info("接收到消息:[{}]", new String(body, StandardCharsets.UTF_8));
                        }
                    };
                    // 推模式
                    consumerChannel.basicConsume("simple-queue", true, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "consumer-" + i).start();
        }

    }
}
