package com.sky.service;

/**
 * 营业状态管理
 */
public interface ShopService {
    /**
     * 设置营业状态
     *
     * @param status
     */
    void EnableAndDisable(Integer status);

    /**
     * 获取营业状态
     *
     * @return
     */
    Integer GetShopStatus();
}
