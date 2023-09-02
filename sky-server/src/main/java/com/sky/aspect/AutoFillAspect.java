package com.sky.aspect;

/**
 * @Author Tians
 * @Date 2023/9/1 14:39
 * @Version 17
 */

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自定义切面类，完成自动填充字段的配置
 */
//切面相关
@Aspect
//放入容器
@Component
//日志相关
@Slf4j
public class AutoFillAspect {

    /**
     * 切点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 通知方法
     * @param joinPoint
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        // 先调试，看能否进入切面 **需要先加注解在需要增强的方法上
        log.info("进入切面,开始公共字段自动填充...");
        //获取到当前被拦截的方法上的数据库操作类型
        //1.方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //2.获得方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //3.最终获得数据库操作类型
        OperationType operationType = autoFill.value();
        //获取到当前被拦截的方法的参数--实体对象
        // 如果没有参数，那么结束程序
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        // 有参数，就把参数取出来
        Object entity = args[0];
        //准备赋值的数据
        // 1.更新时间--创建时间
        LocalDateTime localDateTime = LocalDateTime.now();
        // 2.创建人--修改人
        Long currentId = BaseContext.getCurrentId();
        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        // 1.四个操作
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //反射赋值
                setCreateUser.invoke(entity,currentId);
                setCreateTime.invoke(entity,localDateTime);
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,localDateTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                // 2.两个操作
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //反射赋值
                setUpdateTime.invoke(entity,localDateTime);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
