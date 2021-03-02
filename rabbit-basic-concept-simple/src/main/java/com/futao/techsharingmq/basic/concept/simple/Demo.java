package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ简单示例
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/19
 */
@Slf4j
public class Demo {

    public static void connection() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //connectionFactory.setUsername("futao");
        //connectionFactory.setPassword("123456789");
        connectionFactory.setVirtualHost("/tech-sharing");
        //connectionFactory.setHost("localhost");
        //connectionFactory.setPort(5672);
        connectionFactory.setUri("amqp://futao:123456789@localhost:5672");
        Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();

        //channel和connection都可以设置shutdow监听器
        channel.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException e) {
                log.info("关闭...", e);
            }
        });
        connection.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException e) {
                log.info("关闭...", e);
            }
        });

        // 监听被return的消息
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return returnMessage) {
                log.info("消息被退回:{}", returnMessage);
            }
        });

        // 定义交换机
        channel.exchangeDeclare("X_SIMPLE_NO_MATCH_QUEUE", BuiltinExchangeType.TOPIC, false, true, new HashMap<>(0));

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
        // 向Exchange投递mandatory为true的消息
        channel.basicPublish("X_NO_MATCH_QUEUE", "", true, false, basicProperties, "123".getBytes(StandardCharsets.UTF_8));

        TimeUnit.MINUTES.sleep(5L);

        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, KeyManagementException, TimeoutException, URISyntaxException, IOException {
        Demo.connection();
    }
}
