package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端套餐管理
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "C端套餐接口文档")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * cacheable:执行方法前，查询缓存是否有数据，有则从缓存中取数据，没有则执行方法，把方法返回的数据放入缓存
     *
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#categoryId")
    public Result<List<Setmeal>> selectListByCategoryId(@RequestParam Long categoryId) {
        List<Setmeal> setmeal = setmealService.selectListByCategoryId(categoryId);
        return Result.success(setmeal);
    }

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param id
     * @return
     */
    @ApiOperation("根据套餐id查询包含的菜品")
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> selectListBySetmealId(@PathVariable Long id) {
        List<DishItemVO> list = setmealService.selectListBySetmealId(id);
        return Result.success(list);
    }

}
