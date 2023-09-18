package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    UserMapper userMapper;


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
}
