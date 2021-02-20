package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/19
 */
@Slf4j
public class RabbitMqUtils {

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
        final AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();
        TimeUnit.MINUTES.sleep(3L);


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

            }
        });
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, KeyManagementException, TimeoutException, URISyntaxException, IOException {
        RabbitMqUtils.connection();
    }
}
