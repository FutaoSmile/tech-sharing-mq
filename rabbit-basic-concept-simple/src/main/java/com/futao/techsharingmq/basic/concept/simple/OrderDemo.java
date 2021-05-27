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
 * @author ft <futao@mysteel.com>
 * @date 2021/4/13
 */
@Slf4j
public class OrderDemo {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://futao:123456789@localhost:5672");
        connectionFactory.setVirtualHost("/tech-sharing");
        // 创建TCP连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义交换机
        channel.exchangeDeclare("order-exchange-topic", BuiltinExchangeType.TOPIC, true, false, false, null);

        // 定义队列-小定
        channel.queueDeclare("pre-order-queue", true, false, false, null);
        // 定义队列-大定
        channel.queueDeclare("official-order-queue", true, false, false, null);
        // 定义队列-所有定单
        channel.queueDeclare("all-order-queue", true, false, false, null);

        // 绑定
        channel.queueBind("pre-order-queue", "order-exchange-topic", "order.pre");
        channel.queueBind("official-order-queue", "order-exchange-topic", "order.official");
        channel.queueBind("all-order-queue", "order-exchange-topic", "order.*");

        log.info("-----");

        // 生产者发送消息
//        new Thread(() -> {
//            // 小定
//            for (int i = 0; i < 10; i++) {
//                try {
//                    String msg = "preOrder-" + i;
//                    channel.basicPublish("order-exchange-topic", "order.pre", new AMQP.BasicProperties(), msg.getBytes(StandardCharsets.UTF_8));
//                    log.info("消息[{}]投递成功", msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            // 大定
//            for (int i = 0; i < 10; i++) {
//                try {
//                    String msg = "officialOrder-" + i;
//                    channel.basicPublish("order-exchange-topic", "order.official", new AMQP.BasicProperties(), msg.getBytes(StandardCharsets.UTF_8));
//                    log.info("消息[{}]投递成功", msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "producer").start();

//        byGet(connection);
        byConsumer(connection);
    }

    /**
     * 消费者通过主动get的方式消费
     *
     * @param connection
     */
    private static void byGet(Connection connection) {
        // 消费消息-小定
        new Thread(() -> {
            try {
                // 一条消息
                GetResponse response = connection.createChannel().basicGet("pre-order-queue", true);
                log.info("get到小定:[{}]", new String(response.getBody(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "pre-consumer").start();

        // 消费消息-大定
        new Thread(() -> {
            // 一条消息
            try {
                GetResponse response = connection.createChannel().basicGet("official-order-queue", true);
                log.info("get到大定:[{}]", new String(response.getBody(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "official-consumer").start();

        // 消费消息-所有订单
        new Thread(() -> {
            // 一条消息
            try {
                GetResponse response = connection.createChannel().basicGet("all-order-queue", true);
                log.info("get到订单:[{}]", new String(response.getBody(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "all-consumer").start();
    }

    /**
     * 推模式消费消息
     *
     * @param connection
     */
    private static void byConsumer(Connection connection) {
        // 小定
        new Thread(() -> {
            try {
                // 创建消费者通道
                Channel channel = connection.createChannel();
                // 检查队列是否存在
                channel.queueDeclarePassive("pre-order-queue");
                // 每次拉取的消息数量
                channel.basicQos(1);
                channel.basicConsume("pre-order-queue", true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        log.info("接收到小定:[{}]", new String(body, StandardCharsets.UTF_8));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "pre-consumer").start();
        // 小定
        new Thread(() -> {
            try {
                // 创建消费者通道
                Channel channel = connection.createChannel();
                // 每次拉取的消息数量
                channel.basicQos(1);
                channel.basicConsume("official-order-queue", true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        log.info("接收到大定:[{}]", new String(body, StandardCharsets.UTF_8));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "pre-consumer").start();

        // 所有订单
        new Thread(() -> {
            try {
                // 创建消费者通道
                Channel channel = connection.createChannel();
                // 每次拉取的消息数量
                channel.basicQos(1);
                channel.basicConsume("all-order-queue", true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        log.info("接收到订单:[{}]", new String(body, StandardCharsets.UTF_8));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "all-consumer").start();
    }
}
