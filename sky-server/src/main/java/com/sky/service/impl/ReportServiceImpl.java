package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrderMapper orderMapper;


    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 时间列表
        // 年月日用LocalDate表示
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        // 营业额列表
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : localDateList) {
            // 获取当天最早的时间和最晚的时间
            LocalDateTime timeMin = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime timeMax = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", timeMin);
            map.put("end", timeMax);
            // 执行查询
            Double turnOver = reportMapper.sumByMap(map);
            turnOver = turnOver == null ? 0.0 : turnOver;
            turnoverList.add(turnOver);
            //
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(localDateList, ','))
                .turnoverList(StringUtils.join(turnoverList, ','))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        Double number = 0.0;
        // 日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 总用户列表
        List<Double> totalUser = new ArrayList<>();
        // 新增用户列表
        List<Double> newUser = new ArrayList<>();

        for (LocalDate date : dateList) {
            // 取出每天的起止时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 每日新增
            Double userCount = userMapper.countUsers(beginTime, endTime);
            newUser.add(userCount);
            // 截止到今天的总人数
            userCount += number;
            totalUser.add(userCount);
            number = userCount;
        }
        //
        return new UserReportVO(StringUtils.join(dateList, ','),
                StringUtils.join(totalUser, ','),
                StringUtils.join(newUser, ','));
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        // 日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 每日订单总数列表
        List<Integer> totalOrderDay = new ArrayList<>();
        // 每日有效订单总数列表
        List<Integer> validOrderDay = new ArrayList<>();
        Map map = new HashMap<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            map.put("begin", beginTime);
            map.put("end", endTime);
            // 当天订单总数
            Integer orderNum = orderMapper.countOrder(map);
            totalOrderDay.add(orderNum);
            // 当天有效订单总数
            map.put("status", Orders.COMPLETED);
            Integer validNum = orderMapper.countOrder(map);
            validOrderDay.add(validNum);
            map.clear();
        }
        // 订单总数
        Integer countOrder = orderMapper.countOrder(new HashMap<>());
        // 有效订单总数
        map.put("status", Orders.COMPLETED);
        Integer validOrder = orderMapper.countOrder(map);
        // 订单完成率
        double completeRote = (validOrder) / (double) countOrder;
        //
        return new OrderReportVO(
                StringUtils.join(dateList, ','),
                StringUtils.join(totalOrderDay, ','),
                StringUtils.join(validOrderDay, ','),
                countOrder, validOrder, completeRote
        );
    }

    /**
     * 查询销量排名top10
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        // 阶段时间的最早最晚
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        // 查找
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.top10(beginTime, endTime);
        // goodsSalesDTOList中每个GoodsSalesDTO的name属性
        String nameList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(
                Collectors.toList()), ",");
        // goodsSalesDTOList中每个GoodsSalesDTO的number属性
        String numberList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(
                Collectors.toList()), ',');
        return new SalesTop10ReportVO(nameList, numberList);
    }
}
