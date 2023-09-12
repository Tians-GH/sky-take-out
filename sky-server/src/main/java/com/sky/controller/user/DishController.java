package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @ApiOperation("根据菜品分类id查询菜品")
    @GetMapping("/list")
    public Result<List<DishVO>> selectListWithFlavorByCategoryId(@RequestParam Long categoryId) {
        // 构造key,规则：dish_分类id
        String key = "dish_" + categoryId;
        // 查询是否存在缓存
        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (dishVOList != null && dishVOList.size() > 0) {
            // 存在缓存直接返回数据
            return Result.success(dishVOList);
        }
        List<DishVO> dishList = dishService.selectListWithFlavorByCategoryId(categoryId);
        // 如果没有则查询数据库重新写入Redis
        redisTemplate.opsForValue().set(key, dishList);
        return Result.success(dishList);
    }
}
