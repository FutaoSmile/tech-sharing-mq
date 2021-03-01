package com.futao.techsharingmq.rpcframework.util;

import java.util.UUID;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
public class Utils {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
