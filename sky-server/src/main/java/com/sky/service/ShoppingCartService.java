package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 增加到购物车
     *
     * @param shoppingCartDTO
     */
    void addToShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     *
     * @return
     */
    List<ShoppingCart> selectList();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();
}