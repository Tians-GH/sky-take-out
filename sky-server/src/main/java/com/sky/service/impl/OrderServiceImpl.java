package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;


    @Override
    public OrderSubmitVO orderSubmit(OrdersSubmitDTO ordersSubmitDTO) {
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        Long userId = BaseContext.getCurrentId();
        Orders orders = new Orders();

        // 地址薄是否有地址
        AddressBook addressBook = addressBookMapper.selectById(addressBookId, userId);
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 购物车是否有商品
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(userId);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 插入数据到order
        // 复制传过来的数据到orders表
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        // 订单号：将当前系统时间的毫秒数转换为字符串。
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(userId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());
        orderMapper.insert(orders);

        // 插入数据到order_detail
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orders.getId());
        for (ShoppingCart shoppingCart : shoppingCarts) {
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetailMapper.insert(orderDetail);
        }
        // 清除购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return orderSubmitVO;
    }
}
