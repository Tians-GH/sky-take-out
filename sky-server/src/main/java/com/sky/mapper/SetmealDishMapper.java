package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author Tians
 * @Date 2023/9/1 10:30
 * @Version 17
 */

/**
 * 套餐菜品关联表
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询关联表
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where dish_id = #{id}")
    SetmealDish selectByDishId(Long id);

    /**
     * 新增菜品
     * @param setmealDish
     */
    void insert(SetmealDish setmealDish);
}
