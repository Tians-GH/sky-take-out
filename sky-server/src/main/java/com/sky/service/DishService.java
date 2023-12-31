package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除/批量删除菜品
     *
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 启用，禁用菜品
     *
     * @param status
     * @param id
     */
    void enableAndDisable(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     *
     * @param id
     * @return
     */
    List<Dish> selectListByCategoryId(Long id);

    /**
     * 根据分类id查询菜品和菜品对应的口味
     *
     * @param categoryId
     * @return
     */
    List<DishVO> selectListWithFlavorByCategoryId(Long categoryId);
}
