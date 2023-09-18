package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入数据
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 获得订单号
     *
     * @param userId
     */
    @Select("select * from orders where user_id = #{userId}")
    Orders getNumber(Long userId);

    /**
     * 查询历史订单
     *
     * @param
     * @return
     */
    Page<Orders> queryOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    List<Orders> queryconditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计：待接单
     */
    @Select("select count(*) from orders where status = #{statusNum}")
    Integer countStatusIsPendingOrder(Integer statusNum);

    /**
     * 各个状态的订单数量统计：已经接单
     *
     * @param statusNum
     */
    @Select("select count(*) from orders where status = #{statusNum}")
    Integer countStatusIsTakeOrder(Integer statusNum);

    /**
     * 各个状态的订单数量统计：派送中
     *
     * @param statusNum
     */
    @Select("select count(*) from orders where status = #{statusNum}")
    Integer countStatusIsDilivering(Integer statusNum);

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders selectByOrderId(Long id);

    /**
     * 查询超时订单
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> selectByStatusAndOrderTime(Integer status, LocalDateTime time);
}
