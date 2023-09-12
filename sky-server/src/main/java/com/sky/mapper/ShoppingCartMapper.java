package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查找购物车表，通过user_id|dish_id|setmeal_id
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 通过购物车id更新购物车商品
     *
     * @param shoppingCart1
     */
    void updateById(ShoppingCart shoppingCart1);

    /**
     * 插入数据
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, user_id, dish_id, setmeal_id,dish_flavor, " +
            "number, amount, image, create_time) " +
            "values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor}," +
            "#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);
}
