package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author Tians
 * @Date 2023/9/1 10:30
 * @Version 17
 */

/**
 * 用户
 */
@Mapper
public interface UserMapper {

    /**
     * 插入数据
     *
     * @param user1
     */
    void insert(User user1);

    /**
     * 通过openid查询
     *
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);
}
