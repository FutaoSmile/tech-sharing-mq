package com.futao.techsharingmq.basic.concept;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/4/13
 */
public class Test {
    /**
     * 测试在未指定exchange的情况下，会默认使用`(AMQP default)`作为exchange，routingKey会作为queueName
     *
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws URISyntaxException
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @org.junit.jupiter.api.Test
    public void test1() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://futao:123456789@localhost:5672");
        connectionFactory.setVirtualHost("/tech-sharing");
        // 创建TCP连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();

        new Thread(() -> {
            while (true) {
                try {
                    channel.basicPublish("", "Q_AE", new AMQP.BasicProperties(), "123".getBytes(StandardCharsets.UTF_8));
                    System.out.println("-");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(20L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.HOURS.sleep(1L);
    }
}
