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
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
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
        categoryMapper.updateCategory(categoryDTO);
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
}
