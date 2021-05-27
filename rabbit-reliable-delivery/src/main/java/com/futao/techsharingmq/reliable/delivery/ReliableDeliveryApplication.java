package com.futao.techsharingmq.reliable.delivery;

import com.futao.techsharingmq.reliable.delivery.complete.config.RetryProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/20
 */
@Slf4j
@EnableConfigurationProperties(RetryProperties.class)
@SpringBootApplication
public class ReliableDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReliableDeliveryApplication.class);
    }
}
