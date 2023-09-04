package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        }
        // 3、插入数据
        dishFlavorMapper.insertBatch(flavors);
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
}
