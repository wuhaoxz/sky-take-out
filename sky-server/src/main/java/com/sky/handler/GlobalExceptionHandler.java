package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理SQL异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result handleSQLException(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        log.error("SQL异常信息：{}", message);

        if(message.contains("Duplicate entry")){
            //Duplicate entry 'admin' for key 'employee.idx_username'
            String[] split = message.split("'");
            String s = split[1];//冲突的值
            return Result.error(s+ MessageConstant.ALREADY_EXISTS);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }


    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
