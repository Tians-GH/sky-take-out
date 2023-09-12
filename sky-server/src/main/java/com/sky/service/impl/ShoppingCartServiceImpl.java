package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.*;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    /**
     * 增加到购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addToShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 是哪位用户的购物车
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 购物车中有没有商品
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        // 如果有，则商品数量+1
        if (shoppingCarts != null && shoppingCarts.size() > 0) {
            ShoppingCart shoppingCart1 = shoppingCarts.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            // 更新
            shoppingCartMapper.updateById(shoppingCart1);
            //
        } else {
            // 如果没有，则插入商品，商品数量为1
            // 判断插入的是菜品还是套餐
            Dish dish = dishMapper.selectById(shoppingCart.getDishId());
            // 菜品:填充菜品相关字段
            if (dish != null) {
                // dishid
                shoppingCart.setDishId(dish.getId());
                // name
                shoppingCart.setName(dish.getName());
                // amount
                shoppingCart.setAmount(dish.getPrice());
                // image
                shoppingCart.setImage(dish.getImage());
            } else {
                // 套餐：填充套餐相关字段
                SetmealVO setmealVO = setmealMapper.selectById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmealVO.getName());
                shoppingCart.setSetmealId(setmealVO.getId());
                shoppingCart.setImage(setmealVO.getImage());
                shoppingCart.setAmount(setmealVO.getPrice());
            }
            // 填充公共字段
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 插入数据
            shoppingCartMapper.insert(shoppingCart);
            //
        }

    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> selectList() {
        // 查看谁的购物车
        Long id = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.selectList(id);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        // 清空谁的购物车
        Long id = BaseContext.getCurrentId();
        shoppingCartMapper.clean(id);
    }
}
