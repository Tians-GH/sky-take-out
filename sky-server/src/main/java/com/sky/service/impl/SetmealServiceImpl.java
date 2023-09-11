package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
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

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        log.info("setmealDTO:{}", setmealDTO);
        // 新增套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        log.info("setmeal:{}", setmeal);
        // 更新中间表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(setmealDish);
        }
    }

    /**
     * 分页查询套餐
     *
     * @param setmealDTO
     * @return
     */
    @Override
    public PageResult selectByPage(SetmealPageQueryDTO setmealDTO) {
        // 分页
        PageHelper.startPage(setmealDTO.getPage(), setmealDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.selectByPage(setmealDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        // 如果套餐的状态是起售，则不能删除
        for (Long id : ids) {
            if (setmealMapper.selectById(id).getStatus() == 0) {
                // 删除套餐表
                setmealMapper.deleteById(id);
                // 删除套餐菜品表
                setmealDishMapper.deleteById(id);
            } else {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {

        // 根据id查询菜品数据
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        // 查询套餐数据
        SetmealVO setmealVO = setmealMapper.selectById(id);
        // 合并数据
        setmealVO.setSetmealDishes(setmealDishes);
        //
        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        // 删除菜品数据
        setmealDishMapper.deleteById(setmealDTO.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 更新菜品表数据
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.insert(setmealDish);
        }
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐数据
        setmealMapper.update(setmeal);
        //
    }

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     */
    @Override
    public void enableAndDisable(Integer status, Long id) {
        // 如果所有菜品都是起售才能套餐起售
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        for (SetmealDish setmealDish : setmealDishes) {
            if (dishMapper.selectById(setmealDish.getDishId()).getStatus() == 0) {
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        setmealMapper.enableAndDisable(status, id);
    }

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Setmeal> selectListByCategoryId(Long categoryId) {
        List<Setmeal> setmeals = setmealMapper.selectListByCategoryId(categoryId);
        return setmeals;
    }

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param setmealId
     * @return
     */
    @Override
    public List<DishItemVO> selectListBySetmealId(Long setmealId) {
        List<DishItemVO> list = new ArrayList<>();
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(setmealId);
        for (SetmealDish setmealDish : setmealDishes) {
            Long dishId = setmealDish.getDishId();
            Dish dish = dishMapper.selectById(dishId);
            DishItemVO dishItemVO = DishItemVO.builder()
                    .name(dish.getName())
                    .image(dish.getImage())
                    .description(dish.getDescription())
                    .copies(setmealDish.getCopies())
                    .build();
            list.add(dishItemVO);
        }
        return list;
    }
}
