package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
    // 菜品起售
    public static final Integer STATUS_ENABLE = 1;
    // 菜品停售
    public static final Integer STATUS_DISABLE = 0;

    private static final long serialVersionUID = 1L;

    private Long id;

    // 菜品名称
    private String name;

    // 菜品分类id
    private Long categoryId;

    // 菜品价格
    private BigDecimal price;

    // 图片
    private String image;

    // 描述信息
    private String description;

    // 0 停售 1 起售
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
