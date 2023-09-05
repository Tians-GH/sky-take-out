package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author Tians
 * @Date 2023/9/1 10:30
 * @Version 17
 */

/**
 * 菜单
 */
@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param id
     * @return
     */
    @Select("select count(*) from dish where category_id = #{id}")
    Integer countByCategoryId(long id);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     *分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> selectByPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    /**
     * 删除/批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 更新dish
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 启用，禁用菜品
     * @param status
     * @param id
     */
    @Update("update dish set status = #{status} where id = #{id}")
    void enableAndDisable(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     * @param id
     * @return
     */
    @Select("select * from dish where category_id = #{id}")
    List<Dish> selectListByCategoryId(Long id);
}
