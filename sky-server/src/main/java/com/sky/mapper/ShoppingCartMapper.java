package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 查看购物车
     *
     * @param id
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{id}")
    List<ShoppingCart> selectList(Long id);

    /**
     * 清空购物车
     */
    @Delete("delete from shopping_cart where user_id = #{id}")
    void clean(Long id);

    /**
     * 根据用户id删除
     *
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCart
     */
    void deleteOne(ShoppingCart shoppingCart);

}
