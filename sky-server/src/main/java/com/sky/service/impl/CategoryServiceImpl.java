package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //复制
        BeanUtils.copyProperties(categoryDTO,category);
        //创建时间
        LocalDateTime createDateTime = LocalDateTime.now();
        //更新时间
        LocalDateTime updateDateTime = LocalDateTime.now();
        //创建人
        Long createId = BaseContext.getCurrentId();
        //修改人
        Long updateId = BaseContext.getCurrentId();
        //自动属性要加上去：创建人，创建时间...
        category.setStatus(0);
        category.setCreateUser(createId);
        category.setCreateTime(createDateTime);
        category.setUpdateTime(updateDateTime);
        category.setUpdateUser(updateId);
        categoryMapper.save(category);
    }

    @Override
    public PageResult selectPageByCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.selectPageByCategory(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //更新人
        category.setUpdateUser(BaseContext.getCurrentId());
        //更新时间
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCategory(category);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    @Override
    public void enableAndDisable(Integer status, long id) {
        categoryMapper.enableAndDisable(status,id);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(long id) {
        ///查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有菜品，不能删除，抛出一个删除异常
            throw new  DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if(count > 0){
            //当前分类下有套餐，不能删除，抛出一个删除异常
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }
}
