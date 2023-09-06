package com.sky.service.impl;

import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 营业状态管理
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    // 定义Rediskey
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     *
     * @param status
     */
    @Override
    public void EnableAndDisable(Integer status) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY, status);
    }

    /**
     * 获取营业状态
     *
     * @return
     */
    @Override
    public Integer GetShopStatus() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object status = valueOperations.get(KEY);
        return (Integer) status;
    }
}
