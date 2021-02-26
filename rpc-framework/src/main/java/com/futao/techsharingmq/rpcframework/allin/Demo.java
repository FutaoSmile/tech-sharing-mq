package com.futao.techsharingmq.rpcframework.allin;

import com.alibaba.fastjson.JSONObject;
import com.futao.techsharingmq.rpcframework.util.Utils;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
@Slf4j
@Component
public class Demo {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void request() {
        JSONObject requestBody = new JSONObject().fluentPut("id", 1);
        String requestId = Utils.uuid();
        log.info("发起请求:请求ID:[{}]报文:[{}]", requestId, requestBody);
        rabbitTemplate.convertAndSend(Declare.RPC_EXCHANGE_NAME, "", requestBody, new CorrelationData(requestId));
        rabbitTemplate.execute(channel -> {
            AMQP.Queue.DeclareOk queueDeclare = channel.queueDeclare();
            queueDeclare.getQueue();
            return queueDeclare;
        });
    }
}
