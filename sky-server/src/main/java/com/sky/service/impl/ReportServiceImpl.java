package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    WorkspaceService workspaceService;


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
        Integer number = 0;
        // 日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 总用户列表
        List<Integer> totalUser = new ArrayList<>();
        // 新增用户列表
        List<Integer> newUser = new ArrayList<>();

        for (LocalDate date : dateList) {
            // 取出每天的起止时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 每日新增
            Integer userCount = userMapper.countUsers(beginTime, endTime);
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

    /**
     * 导出Excel报表
     *
     * @param response
     */
    @Override
    public void export(HttpServletResponse response) throws IOException {
        // 加载设置好的Excel模板
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("template/运营数据报表模板.xlsx");
        // 概括数据（最近30天）
        LocalDate begin = LocalDate.now().minusDays(30);    // 获取今天-30
        LocalDate end = LocalDate.now();
        BusinessDataVO businessDataVO = workspaceService.businessData(
                LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        // 基于模板创建新的表格
        XSSFWorkbook excel = new XSSFWorkbook(inputStream);
        // 获取sheet1
        XSSFSheet sheet1 = excel.getSheet("sheet1");
        // 填充数据吧
        sheet1.getRow(1).getCell(1).setCellValue(begin + "至" + end);
        // 营业额
        sheet1.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
        // 订单完成率
        sheet1.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        // 新增用户数
        sheet1.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());
        // 有效订单
        sheet1.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        // 平均客单价
        sheet1.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());
        // 详细数据（30天的每一天）
        int row = 7;
        while (!begin.equals(end)) {
            BusinessDataVO businessDataVO1 = workspaceService
                    .businessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(begin, LocalTime.MAX));
            // 日期
            sheet1.getRow(row).getCell(1).setCellValue(String.valueOf(begin));
            // 营业额
            sheet1.getRow(row).getCell(2).setCellValue(businessDataVO1.getTurnover());
            // 有效订单
            sheet1.getRow(row).getCell(3).setCellValue(businessDataVO1.getValidOrderCount());
            // 订单完成率
            sheet1.getRow(row).getCell(4).setCellValue(businessDataVO1.getOrderCompletionRate());
            // 平均客单价
            sheet1.getRow(row).getCell(5).setCellValue(businessDataVO1.getUnitPrice());
            // 新增用户数
            sheet1.getRow(row).getCell(6).setCellValue(businessDataVO1.getNewUsers());
            begin = begin.plusDays(1);
            row++;
            //
        }
        // 通过输出流将文件下载到客户端浏览器
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        // 写入输出流
        excel.write(outputStream);
        // 写入文件要刷新
        outputStream.flush();
        // 关闭资源
        outputStream.close();
        excel.close();
        //
    }
}
