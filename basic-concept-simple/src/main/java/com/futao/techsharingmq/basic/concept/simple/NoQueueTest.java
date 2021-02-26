package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/25
 */
public class NoQueueTest {
    public static void main(String[] args) throws IOException {
        Channel channel = MqChannelUtils.getChannel();

        String x_no_queue = "x_no_queue";
        channel.exchangeDeclare(x_no_queue, BuiltinExchangeType.FANOUT, false, false, false, null);

        for (int i = 0; i < 5; i++) {
            channel.basicPublish(x_no_queue, "", null, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
        }

    }
}
