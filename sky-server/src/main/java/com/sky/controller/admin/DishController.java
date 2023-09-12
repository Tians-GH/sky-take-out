package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author Tians
 * @Date 2023/9/2 16:48
 * @Version 17
 */
@Api(tags = "菜品接口")
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 清除缓存
     *
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.save(dishDTO);
        // 清除缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> selectByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("查询条件：{}", dishPageQueryDTO);
        PageResult page = dishService.selectByPage(dishPageQueryDTO);
        return Result.success(page);
    }

    /**
     * 删除/批量删除菜品
     *
     * @param ids
     * @return
     */
    @ApiOperation("删除/批量删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("查询条件：{}", ids);
        dishService.delete(ids);
        // 清除缓存，因为批量删除菜品，可能不止一个分类下的菜品，所以全部删除菜品缓存，错杀一万
        String key = "dish_*";
        cleanCache(key);
        return Result.success();
    }

    /**
     * 根据id查询菜品回显
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id) {
        log.info("查询条件：{}", id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("查询条件：{}", dishDTO);
        dishService.updateDish(dishDTO);
        // 清除缓存，因为更新菜品，可能不止一个分类下的菜品，所以全部删除菜品缓存，错杀一万
        String key = "dish_*";
        cleanCache(key);
        return Result.success();
    }

    /**
     * 启用，禁用菜品
     *
     * @param id
     * @return
     */
    @ApiOperation("启用，禁用菜品")
    @PostMapping("/status/{status}")
    public Result enableAndDisable(@PathVariable Integer status, Long id) {
        dishService.enableAndDisable(status, id);
        // 清除缓存，因为禁用菜品，可能不止一个分类下的菜品，所以全部删除菜品缓存，错杀一万
        String key = "dish_*";
        cleanCache(key);
        return Result.success();
    }

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
