package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 插入数据
     *
     * @param orderDetail
     */
    void insert(OrderDetail orderDetail);

    /**
     * 通过id查询
     *
     * @param id
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> selectById(Long id);
}
