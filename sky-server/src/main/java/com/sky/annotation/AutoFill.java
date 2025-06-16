package com.sky.annotation;

import com.sky.constant.AutoFillConstant;
import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义公共字段填充注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {
    //用来标识新增还是修改
    OperationType value();
}
