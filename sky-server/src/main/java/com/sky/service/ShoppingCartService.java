package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {
    /**
     * 增加到购物车
     *
     * @param shoppingCartDTO
     */
    void addToShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
