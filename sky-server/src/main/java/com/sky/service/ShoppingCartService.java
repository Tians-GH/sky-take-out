package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.OrderDetail;
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
     * 增加上一个订单的商品到购物车
     *
     * @param
     */
    void addToShoppingCart(OrderDetail orderDetail);

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

    /**
     * 删除购物车中一个商品
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
