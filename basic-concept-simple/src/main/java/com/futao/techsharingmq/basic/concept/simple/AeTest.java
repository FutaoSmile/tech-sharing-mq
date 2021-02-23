package com.futao.techsharingmq.basic.concept.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 备份交换机测试AlternateExchange
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/23
 */
@Slf4j
public class AeTest {
    /**
     * 获取Channel
     */
    private static final Channel CHANNEL = MqChannelUtils.getChannel();
    /**
     * 备份交换机
     */
    private static final String X_AE = "X_AE";
    /**
     * 备份交换机绑定的队列
     */
    private static final String Q_AE = "Q_AE";

    /**
     * 正常业务的交换机
     */
    private static final String X_1 = "X_1";


    public static void main(String[] args) throws IOException {
        // 定义备份交换机-其实也是一个正常的交换机
        CHANNEL.exchangeDeclare(X_AE, BuiltinExchangeType.FANOUT, true);
        // 定义队列
        CHANNEL.queueDeclare(Q_AE, true, false, false, null);
        // 绑定
        CHANNEL.queueBind(Q_AE, X_AE, "");


        HashMap<String, Object> arguments = new HashMap<>();
        // 绑定的备份交换机
        arguments.put("alternate-exchange", X_AE);
        // 定义交换机
        CHANNEL.exchangeDeclare(X_1, BuiltinExchangeType.TOPIC, false, false, arguments);

        // 添加监听器，看看是否还会return消息
        CHANNEL.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return returnMessage) {
                log.error("消息被退回{}", returnMessage);
            }
        });

        // 尝试向交换机发送消息（无法路由）- mandatory参数无效
        CHANNEL.basicPublish(X_1, "", false, false,
                new AMQP.BasicProperties(), "阿依古丽".getBytes(StandardCharsets.UTF_8));
    }
}
