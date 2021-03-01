package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 测试队列没有绑定交换机-向空交换机发送
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/25
 */
public class NoExchangeTest {
    public static void main(String[] args) {
        Channel channel = MqChannelUtils.getChannel();
        try {
            String q_no_exchange = "q_no_exchange";
            channel.queueDeclare(q_no_exchange, false, false, false, null);
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("", q_no_exchange, null, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
