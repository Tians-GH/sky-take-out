package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.*;

import java.time.LocalDateTime;

public interface OrderService {
    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult queryOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    OrderStatisticsVO statisticStatus();

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    OrderVOO selectByOrderId(Long id);

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     *
     * @param id
     */
    void deliveryOrder(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void completeOrder(Long id);

    /**
     * 查询订单详情
     *
     * @param id
     */
    OrderVOO orderDetail(Long id);

    /**
     * 再来一单
     *
     * @param id
     */
    void repetitionOrder(Long id);

    /**
     * 催单
     *
     * @param id
     */
    void reminderOrder(Long id);

    /**
     * 查询今日运营数据
     *
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     *
     * @return
     */
    OrderOverViewVO overviewOrders();

    /**
     * 查询菜品总览
     *
     * @return
     */
    DishOverViewVO overviewDishes();

    /**
     * 查询套餐总览
     *
     * @return
     */
    SetmealOverViewVO overviewSetmeals();
}
