package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 营业状态管理
 */
@RestController
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "营业状态管理接口文档")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 设置营业状态
     *
     * @param status
     * @return
     */
    @ApiOperation("设置营业状态")
    @PutMapping("/{status}")
    public Result EnableAndDisable(@PathVariable Integer status) {
        shopService.EnableAndDisable(status);
        return Result.success();
    }

    /**
     * 获取营业状态
     *
     * @param
     * @return
     */
    @ApiOperation("获取营业状态")
    @GetMapping("/{status}")
    public Result<Integer> GetShopStatus() {
        Integer status = shopService.GetShopStatus();
        return Result.success(status);
    }
}
