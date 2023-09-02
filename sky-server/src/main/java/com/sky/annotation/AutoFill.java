package com.sky.annotation;

/**
 * @Author Tians
 * @Date 2023/9/1 14:31
 * @Version 17
 */

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注解
 */
// 在哪里使用：方法上
@Target(ElementType.METHOD)
// 在什么时候使用：在程序运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //有哪些成员
    OperationType value();
}
