package com.futao.techsharingmq.rpcframework.demo.business.consumer;

import com.futao.techsharingmq.rpcframework.allin.RpcRequestSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
@Slf4j
@Service
public class UserConsumer {

    @Autowired
    private RpcRequestSender rpcRequestSender;

    @PostConstruct
    public void printUserInfo() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                log.info("发起请求....");
                Object result = rpcRequestSender.request(new RpcRequestSender.RequestModel("userInfo", finalI));
                log.info("用户信息为:{}", result);
            }).start();
        }
    }

}
