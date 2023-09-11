package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 用户
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    WeChatProperties weChatProperties;

    // 微信登录接口号
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 获得openID
        String openId = getOpenId(userLoginDTO.getCode());
        // 判断openId是否为空，为空则登录失败
        if (openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断是否为新用户
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 准备发送请求，获得openid
     *
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        HashMap<String, String> map = new HashMap<>();
        // 填充条件字段
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        // 发送get请求 获得返回的数据
        String doGet = HttpClientUtil.doGet(WX_LOGIN, map);
        // 把字符串数据转换成了一个JSONObject对象，可以通过键获取值
        JSONObject jsonObject = JSONObject.parseObject(doGet);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
