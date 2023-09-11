package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tians
 * @Date 2023/9/2 16:48
 * @Version 17
 */
@Api(tags = "C端菜品接口")
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @ApiOperation("根据菜品分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> selectListByCategoryId(@RequestParam Long categoryId) {
        List<Dish> dishList = dishService.selectListByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
