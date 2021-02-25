package com.futao.techsharingmq.rpcframework.demo.business.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.futao.techsharingmq.rpcframework.demo.business.api.UserService;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
public class UserServiceImpl implements UserService {
    @Override
    public JSON userInfo() {
        return new JSONObject().fluentPut("username", "付韬").fluentPut("address", "浙江杭州");
    }
}
