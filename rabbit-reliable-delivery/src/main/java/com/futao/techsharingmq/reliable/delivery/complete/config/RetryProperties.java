package com.futao.techsharingmq.reliable.delivery.complete.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/5/27
 */
@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq.retry")
public class RetryProperties {
    private int maxTimes;
    private Duration retryInterval;

    public int getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }

    public Duration getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Duration retryInterval) {
        this.retryInterval = retryInterval;
    }
}
