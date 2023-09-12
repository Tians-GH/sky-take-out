package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车管理")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 小程序登录
     *
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("增加商品到购物车")
    @PostMapping("/add")
    public Result addToShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("shoppingCartDTO:{}", shoppingCartDTO);
        shoppingCartService.addToShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> selectList() {
        List<ShoppingCart> list = shoppingCartService.selectList();
        return Result.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result cleanShoppingCart() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
}
