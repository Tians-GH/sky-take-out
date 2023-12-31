package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 工作台管理
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台管理接口文档")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    /**
     * 查询今日运营数据
     *
     * @param
     * @return
     */
    @ApiOperation("查询今日运营数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workspaceService.businessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 查询订单管理数据
     *
     * @param
     * @return
     */
    @ApiOperation("查询订单管理数据")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders() {
        OrderOverViewVO orderOverViewVO = workspaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }

    /**
     * 查询菜品总览
     *
     * @param
     * @return
     */
    @ApiOperation("查询菜品总览")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes() {
        DishOverViewVO dishOverViewVO = workspaceService.overviewDishes();
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询套餐总览
     *
     * @param
     * @return
     */
    @ApiOperation("查询套餐总览")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        SetmealOverViewVO setmealOverViewVO = workspaceService.overviewSetmeals();
        return Result.success(setmealOverViewVO);
    }

}
