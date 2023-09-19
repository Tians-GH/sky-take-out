package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVOO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    UserMapper userMapper;
    @Autowired
    WebSocketServer webSocketServer;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 下单
     *
     * @param ordersSubmitDTO
     * @return
     */
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

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // // // 当前登录用户id
        // // Long userId = BaseContext.getCurrentId();
        // // User user = userMapper.getById(userId);
        // //
        // // // 调用微信支付接口，生成预支付交易单
        // // JSONObject jsonObject = WeChatPayUtil.pay(
        // //         ordersPaymentDTO.getOrderNumber(), // 商户订单号
        // //         new BigDecimal(0.01), // 支付金额，单位 元
        // //         "苍穹外卖订单", // 商品描述
        // //         user.getOpenid() // 微信用户的openid
        // // );
        // //
        // // if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
        // //     throw new OrderBusinessException("该订单已支付");
        // // }
        //
        // // OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        // // vo.setPackageStr(jsonObject.getString("package"));
        // OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        // orderPaymentVO.setNonceStr("ABC123");
        // orderPaymentVO.setPaySign("XYZ789");
        // orderPaymentVO.setTimeStamp("2023-07-04 12:34:56");
        // orderPaymentVO.setSignType("MD5");
        // orderPaymentVO.setPackageStr("prepay_id=123456789");
        //
        // return orderPaymentVO;
        // 当前登录用户id
        // Long userId = BaseContext.getCurrentId();
        // User user = userMapper.getById(userId);
        // // 调用微信支付接口，生成预支付交易单
        // JSONObject jsonObject = WeChatPayUtil.pay(
        //         ordersPaymentDTO.getOrderNumber(), // 商户订单号
        //         new BigDecimal(0.01), // 支付金额，单位 元
        //         "苍穹外卖订单", // 商品描述
        //         user.getOpenid() // 微信用户的openid
        // );
        //
        // JSONObject jsonObject = new JSONObject();
        //
        // if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
        //     throw new OrderBusinessException("该订单已支付");
        // }
        // OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        // vo.setPackageStr(jsonObject.getString("package"));

        paySuccess(ordersPaymentDTO.getOrderNumber());

        return null;

    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED) // 订单状态
                .payStatus(Orders.PAID) // 订单支付状态
                .checkoutTime(LocalDateTime.now()) // 支付时间
                .build();
        orderMapper.update(orders);

        Map map = new HashMap<>();
        map.put("type", 1); // 1:来单提醒，2：客户催单提醒。
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号" + outTradeNo);
        String jsonString = JSON.toJSONString(map);
        // 广播支付完成信号到前后端
        webSocketServer.sendToAllClient(jsonString);
        //
    }

    /**
     * 查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryOrder(OrdersPageQueryDTO ordersPageQueryDTO) {
        OrderVOO orderVOO = new OrderVOO();
        List<OrderVOO> vooList = new ArrayList<>();

        // 先设置分页条件
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 查询出订单的记录
        Page<Orders> page = orderMapper.queryOrder(ordersPageQueryDTO);

        long total = page.getTotal();
        List<Orders> ordersList = page.getResult();

        // 将ordersList的值复制到vooList
        for (Orders orders1 : ordersList) {
            BeanUtils.copyProperties(orders1, orderVOO);
            vooList.add(orderVOO);
            // 因为list里储存的是orderVOO的地址，使用每次复制了都要用一个新的地址，不然就是在同一个地址上不停赋值，
            orderVOO = new OrderVOO();
        }

        // 通过订单id查出对呀订单的商品
        for (OrderVOO voo : vooList) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectById(voo.getId());
            voo.setOrderDetailList(orderDetails);
            log.info("voo:{}", voo);
        }

        return new PageResult(total, vooList);
    }

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        OrderVOO orderVOO = new OrderVOO();
        List<OrderVOO> vooList = new ArrayList<>();
        // 先设置分页条件
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        // 查询出订单的记录
        Page<Orders> page = orderMapper.queryOrder(ordersPageQueryDTO);

        long total = page.getTotal();
        List<Orders> ordersList = page.getResult();

        // 将ordersList的值复制到vooList
        for (Orders orders1 : ordersList) {
            BeanUtils.copyProperties(orders1, orderVOO);
            vooList.add(orderVOO);
            // 因为list里储存的是orderVOO的地址，使用每次复制了都要用一个新的地址，不然就是在同一个地址上不停赋值，
            orderVOO = new OrderVOO();
        }

        return new PageResult(total, vooList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @Override
    public OrderStatisticsVO statisticStatus() {

        Integer pendingOrder = orderMapper.countStatusIsPendingOrder(Orders.TO_BE_CONFIRMED);
        Integer isTakeOrder = orderMapper.countStatusIsTakeOrder(Orders.CONFIRMED);
        Integer dilivering = orderMapper.countStatusIsDilivering(Orders.DELIVERY_IN_PROGRESS);
        return new OrderStatisticsVO(pendingOrder, isTakeOrder, dilivering);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVOO selectByOrderId(Long id) {
        Orders orders = orderMapper.selectByOrderId(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectById(id);
        OrderVOO orderVOO = new OrderVOO();
        BeanUtils.copyProperties(orders, orderVOO);
        orderVOO.setOrderDetailList(orderDetailList);
        // 菜品名称们
        String orderDishes = "";
        for (OrderDetail orderDetail : orderDetailList) {
            // 判断是菜品还是套餐
            Long dishId = orderDetail.getDishId();
            Long setmealId = orderDetail.getSetmealId();
            if (dishId != null) {
                Dish dish = dishMapper.selectById(dishId);
                orderDishes = orderDishes + dish.getName() + "*" + orderDetail.getNumber() + ";";
            } else {
                List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(setmealId);
                for (SetmealDish setmealDish : setmealDishes) {
                    orderDishes += setmealDish.getName() + "*" + orderDetail.getNumber() + ";";
                }
            }
        }
        orderVOO.setOrderDishes(orderDishes);
        return orderVOO;
    }

    /**
     * 接单
     *
     * @param ordersConfirmDTO
     */
    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersConfirmDTO, orders);
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.update(orders);
    }

    /**
     * 拒单
     *
     * @param ordersRejectionDTO
     */
    @Override
    public void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersRejectionDTO, orders);
        // 要修改订单的状态为已取消。
        orders.setStatus(Orders.CANCELLED);
        // 设置拒单时间（取消时间）
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersCancelDTO, orders);
        // 要修改订单的状态为已取消。
        orders.setStatus(Orders.CANCELLED);
        // 设置拒单时间（取消时间）
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 派送订单
     *
     * @param id
     */
    @Override
    public void deliveryOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        // 要修改订单的状态为已取消。
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
    }

    /**
     * 完成订单
     *
     * @param id
     */
    @Override
    public void completeOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        // 要修改订单的状态为已完成。
        orders.setStatus(Orders.COMPLETED);
        // 更新送达时间
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderVOO orderDetail(Long id) {
        Orders orders = orderMapper.selectByOrderId(id);
        // 菜品
        List<OrderDetail> orderDetailList = orderDetailMapper.selectById(orders.getId());
        OrderVOO orderVOO = new OrderVOO();
        BeanUtils.copyProperties(orders, orderVOO);
        orderVOO.setOrderDetailList(orderDetailList);
        return orderVOO;
    }

    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    public void repetitionOrder(Long id) {

    }

    /**
     * 催单
     *
     * @param id
     */
    @Override
    public void reminderOrder(Long id) {
        // 判断订单是否存在
        Orders orders = orderMapper.selectByOrderId(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 准备数据
        // 基于WebSocket实现催单
        Map map = new HashMap();
        map.put("type", 2);// 2代表用户催单
        map.put("orderId", id);
        map.put("content", "订单号：" + orders.getNumber());
        // 通过websocket发送消息
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
        //
    }

}
