package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类管理
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
@Api(tags = "C端分类接口文档")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> selectByType(Integer type) {
        List<Category> categories = categoryService.selectByType(type);
        return Result.success(categories);
    }
}
