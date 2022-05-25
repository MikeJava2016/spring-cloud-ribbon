package com.sunshine.utils.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Result<T> implements Serializable {
    private Integer code;

    private String message;

    private T data;

    private String token;

    public static <T> Result<T> success(T data,String message) {
        try {
            return new Result<T>().data(data).code(0).message(new String(message.getBytes(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Result<T> success(T data) {
        return success(data,"操作成功!");
    }

    public static <T> Result<T> success() {
        return new Result<T>().data(null).code(0).message("操作成功!");
    }

    public static <T> Result<T> fail() {
        return fail("操作失败!");
    }

    public static <T> Result<T> fail(String message)  {
       return fail(-1,message);
    }

    public static <T>  Result<T> fail(int code, String message) {
        Result result = new Result<T>().data(null).code(code);
        try {
            return result.message(new String(message.getBytes(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public Result code(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public  Result message(String message) {
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

    public Result token(String token){
        this.token = token;
        return this;
    }

    public String getToken(){
        return token;
    }
}
