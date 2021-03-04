package com.futao.techsharingmq.kafka.basic.declare;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/3
 */
@Configuration
public class Topics {

    public static final String ORDER_TOPIC = "order_topic";

    /**
     * 定义Topic
     *
     * @return
     */
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name(Topics.ORDER_TOPIC)
                // 分区为8
                .partitions(8)
                // 副本为2。这个值要小于或者等于Broker的数量
                .replicas(1)
                .build();
    }
}
