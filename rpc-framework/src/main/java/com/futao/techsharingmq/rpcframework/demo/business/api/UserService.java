package com.futao.techsharingmq.rpcframework.demo.business.api;

import com.alibaba.fastjson.JSON;

/**
 * 用户相关接口
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
public interface UserService {
    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    JSON userInfo();
}
