package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
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

    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 新增菜品
        dishMapper.insert(dish);
        // 同步口味
        // 1、获取主键值
        Long dishId = dish.getId();
        // 2、取出味道数据，为每个数据赋值菜品id
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            // 3、插入数据
            dishFlavorMapper.insertBatch(flavors);
        }
        //
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult selectByPage(DishPageQueryDTO dishPageQueryDTO) {
        // 插件分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.selectByPage(dishPageQueryDTO);
        long total = dishPage.getTotal();
        List<DishVO> records = dishPage.getResult();
        return new PageResult(total,records);
    }

    /**
     * 删除/批量删除菜品
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        // 起售中的菜品不能删除
        for (Long id : ids) {
            if (dishMapper.selectById(id).getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //被套餐关联的菜品不能删除
        for (Long id : ids) {
            if (setmealDishMapper.selectByDishId(id) != null ) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        // 可以一次删除一个菜品，也可以批量删除菜品
        dishMapper.deleteBatch(ids);
        // 删除菜品后，关联的口味数据也需要删除掉
        dishFlavorMapper.deleteBatch(ids);
        //在dish表中删除菜品基本数据时，同时，也要把关联在dish_flavor表中的数据一块删除。
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO selectById(Long id) {
        DishVO dishVO = new DishVO();
        // 查询菜品
        Dish dish = dishMapper.selectById(id);
        // 查询口味
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        // 合并为vo
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        log.info("vo:{}",dishVO);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void updateDish(DishDTO dishDTO) {
        // 因为需要更新更新人和更新时间字段，所有需要dish源表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 更新dish表
        dishMapper.update(dish);
        // 更新flavor表
        // 1、如果有的话，删除flavor数据
        Long id = dishDTO.getId();
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        log.info("ids:{}",ids);
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        if (flavors != null && flavors.size() > 0) {
            dishFlavorMapper.deleteBatch(ids);
        }
        // 2、有新的数据就插入新的数据
        List<DishFlavor> flavors1 = dishDTO.getFlavors();
        // 设置数据
        for (DishFlavor df : flavors1) {
            df.setDishId(dishDTO.getId());
        }
        if (flavors1 != null && flavors1.size() > 0 ) {
            dishFlavorMapper.insertBatch(flavors1);
        }
    }

    /**
     * 启用，禁用菜品
     * @param status
     * @param id
     */
    @Override
    public void enableAndDisable(Integer status, Long id) {
        dishMapper.enableAndDisable(status,id);
    }

    /**
     * 根据分类id查询菜品
     * @param id
     * @return
     */
    @Override
    public List<Dish> selectListByCategoryId(Long id) {
        List<Dish> dishList = dishMapper.selectListByCategoryId(id);
        return dishList;
    }
}
