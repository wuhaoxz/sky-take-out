package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;

import java.time.LocalDateTime;


@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){

        log.info("开始公共字段自动填充...");

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;//方法签名
        Method method = methodSignature.getMethod();//获得方法对象

        AutoFill annotation = method.getDeclaredAnnotation(AutoFill.class);//获得方法对象上声明的注解
        OperationType value = annotation.value();

        Object entity = joinPoint.getArgs()[0];//连接点的第一个实参对象；employee category

        //如果是新增操作
        if(value == OperationType.INSERT){
            //通过反射来补充属性值
            Class<?> aClass = entity.getClass();//得到的是对象的真实类型的字节码对象
            try {
                Method setCreateTime = aClass.getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = aClass.getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setCreateUser = aClass.getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = aClass.getDeclaredMethod("setUpdateUser", Long.class);
                setCreateTime.invoke(entity, LocalDateTime.now());
                setUpdateTime.invoke(entity, LocalDateTime.now());
                setCreateUser.invoke(entity, BaseContext.getCurrentId());
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if(value == OperationType.UPDATE){//如果是更新操作
            //通过反射来补充属性值
            Class<?> aClass = entity.getClass();//得到的是对象的真实类型的字节码对象
            try {
                Method setUpdateTime = aClass.getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = aClass.getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateTime.invoke(entity, LocalDateTime.now());
                setUpdateUser.invoke(entity, BaseContext.getCurrentId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

}
