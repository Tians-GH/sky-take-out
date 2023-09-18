package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时器
 *
 * @author : tians
 * @date : 2023/9/18 9:38
 * @modyified By :
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    OrderMapper orderMapper;

    /**
     * 订单超时未付款定时器
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void orderTimeOutForPay() {
        log.info("订单超时未付款定时器...");
        // 检查未付款的且超时的订单（超时时间设置为15分钟）
        List<Orders> ordersList = orderMapper.selectByStatusAndOrderTime(Orders.PENDING_PAYMENT,
                LocalDateTime.now().plusMinutes(-15));
        // 自动设置其为取消状态，并设置取消原因
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("用户超时付款，自动取消");
                orderMapper.update(orders);
            }
        }
        //
    }

    /**
     * 订单超时未完成定时器
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void orderTimeOutForFinish() {
        log.info("订单超时未完成定时器...");
        // 检查未付款的且超时的订单（超时时间设置为60分钟）
        List<Orders> ordersList = orderMapper.selectByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,
                LocalDateTime.now().plusMinutes(-60));
        // 自动设置其为取消状态，并设置取消原因
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
