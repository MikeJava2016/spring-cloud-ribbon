package com.sunshine.common.exception;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:31
 **/
public class BaseException extends RuntimeException{
    protected String code;
    protected String msg;

    public BaseException() {
        super();
    }
    public BaseException(String message) {
        super(message);
        this.msg=message;
    }

    public BaseException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
}
