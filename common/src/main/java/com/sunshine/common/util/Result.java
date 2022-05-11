package com.sunshine.common.util;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private Integer code;

    private String message;

    private T data;


    public static <T> Result<T> success(T data) {
        return new Result<T>().data(data).code(0).message("操作成功!");
    }

    public static <T> Result<T> success() {
        return new Result<T>().data(null).code(0).message("操作成功!");
    }

    public static <T> com.sunshine.common.util.Result<T> fail() {
        return new Result<T>().data(null).code(-1).message("操作失败!");

    }

    public static <T> com.sunshine.common.util.Result<T> fail(String message) {
        return new  Result<T>().data(null).code(-1).message(message);
    }

    public static <T>  Result<T> fail(int code, String message) {
        return new Result<T>().data(null).code(code).message(message);
    }

    public Integer getCode() {
        return code;
    }

    public com.sunshine.common.util.Result code(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public com.sunshine.common.util.Result message(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public  Result data(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess(){
        return code==0;
    }
}
