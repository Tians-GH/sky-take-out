package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;


    /**
     * 查询今日运营数据
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {
        // 当日订单完成率：已完成订单数/订单总数*100%
        // 当日营业额：已完成订单
        // 当日有效订单数：已完成订单数
        // 当日平均客单价：营业额/已完成订单数
        // 当日新增用户数: 今天时间注册的用户数

        //
        //
        // 当日有效订单数
        Integer finishOrder;
        // 当日营业额 平均客单价 订单完成率
        Double turnover, orderRate, average;

        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        // 今日所有订单
        Integer orderOver = orderMapper.countOrder(map);
        // 新增用户
        Integer userNumber = userMapper.countUsers(begin, end);
        // 完成订单
        map.put("status", Orders.COMPLETED);
        finishOrder = orderMapper.countOrder(map);

        if (orderOver == 0 || finishOrder == 0) {
            finishOrder = 0;
            turnover = 0.0;
            average = 0.0;
            orderRate = 0.0;
            return new BusinessDataVO(turnover, finishOrder, orderRate, average, userNumber);
        }

        orderRate = (double) finishOrder / orderOver;
        turnover = reportMapper.sumByMap(map);
        average = turnover / finishOrder;
        return new BusinessDataVO(turnover, finishOrder, orderRate, average, userNumber);
    }

    /**
     * 查询订单管理数据
     *
     * @return
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        Map map = new HashMap<>();
        // 待接单数量
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countOrder(map);
        // 待派送数量
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countOrder(map);
        // 已完成数量
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countOrder(map);
        // 已取消数量
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countOrder(map);
        // 全部订单
        map.clear();
        Integer allOrders = orderMapper.countOrder(map);
        return new OrderOverViewVO(waitingOrders, deliveredOrders, completedOrders, cancelledOrders, allOrders);
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    @Override
    public DishOverViewVO overviewDishes() {
        Integer enable = dishMapper.countDish(Dish.STATUS_ENABLE);
        Integer disable = dishMapper.countDish(Dish.STATUS_DISABLE);
        return new DishOverViewVO(enable, disable);
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer enable = setmealMapper.countSetmeal(Dish.STATUS_ENABLE);
        Integer disable = setmealMapper.countSetmeal(Dish.STATUS_DISABLE);
        return new SetmealOverViewVO(enable, disable);
    }
}
