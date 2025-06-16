package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据



    public static Result success() {
        Result result = new Result();
        result.code = 1;
        return result;
    }

    public static <M> Result<M> success(M object) {
        Result<M> result = new Result<M>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
