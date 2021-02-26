package com.futao.techsharingmq.rpcframework.allin;

import com.alibaba.fastjson.JSON;
import com.futao.techsharingmq.rpcframework.util.Utils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/25
 */
@Slf4j
@Component
public class RpcRequestSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理器数量
     */
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数量
     */
    private static final int MAXIMUM_POOL_SIZE = AVAILABLE_PROCESSORS * 2;
    /**
     * 线程编号
     */
    private static final AtomicInteger THREAD_NUM = new AtomicInteger(0);
    /**
     * 持有当前正在等待的返回值的线程
     * 请求ID，线程
     */
    private static final ConcurrentHashMap<String, Thread> WAITING_RESPONSE_THREAD_MAP = new ConcurrentHashMap<>((int) (AVAILABLE_PROCESSORS / 0.75D + 1));

    /**
     * 线程池-请求发送
     */
    private static final ExecutorService THREAD_POOL;

    static {
        THREAD_POOL = new ThreadPoolExecutor(
                AVAILABLE_PROCESSORS,
                MAXIMUM_POOL_SIZE,
                1L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread("rpc-request-sender-pool-" + THREAD_NUM.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @PostConstruct
    public void channel() {
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        for (int i = 0; i < 4; i++) {
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 请求ID
                    String correlationId = properties.getCorrelationId();
                    // 请求参数
                    RequestModel parseObject = JSON.parseObject(body, RequestModel.class);
                    log.info("接收到请求,请求ID:[{}],请求参数:[{}]", correlationId, JSON.toJSONString(parseObject));

                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                            .correlationId(correlationId)
                            .build();
                    channel.basicPublish("", properties.getReplyTo(), basicProperties, "响应".getBytes(StandardCharsets.UTF_8));
                }
            };
            try {
                channel.basicConsume(Declare.RPC_QUEUE_NAME, true, consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起请求并返回结果
     *
     * @param requestModel
     * @return
     */
    public Object request(RequestModel requestModel) {
        MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                // replyTo当前客户端的Queue
                .setReplyTo(RabbitTemplateEnhance.QUEUE_NAME)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(StandardCharsets.UTF_8.displayName())
                .build();
        String requestModelJsonStr = JSON.toJSONString(requestModel);
        Message message = new Message(requestModelJsonStr.getBytes(StandardCharsets.UTF_8), messageProperties);
        String requestId = Utils.uuid();
        log.info("发起请求:id:[{}],requestModel:[{}]", requestId, requestModelJsonStr);
        rabbitTemplate.convertAndSend(Declare.RPC_EXCHANGE_NAME, "", message, new CorrelationData(requestId));
        // 阻塞当前线程，等待结果再返回(这个地方可以加入超时机制)
        try {
            synchronized (requestId) {
                requestId.wait();
            }
        } catch (InterruptedException e) {
            log.info("请求失败，线程被中断", e);
        }

        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        final Object[] result = {null};
        try {
            channel.basicConsume(RabbitTemplateEnhance.QUEUE_NAME, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String s = new String(body);
                    log.info("获取到响应:[{}]", s);
                    result[0] = s;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取响应失败", e);
        }
        return result[0];
    }

    /**
     * 封装的请求信息
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        /**
         * 请求的方法
         */
        private String method;
        /**
         * 请求参数
         */
        private Object params;
    }
}
