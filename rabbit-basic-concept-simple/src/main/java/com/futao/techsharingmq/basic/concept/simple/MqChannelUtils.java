package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/23
 */
public class MqChannelUtils {
    private static ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try {
            connectionFactory.setUri("amqp://futao:123456789@localhost:5672");
            connectionFactory.setVirtualHost("/tech-sharing");
            return connectionFactory;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return connectionFactory;
    }


    public static Channel getChannel() {
        try {
            Connection connection = MqChannelUtils.connectionFactory().newConnection();
            return connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
