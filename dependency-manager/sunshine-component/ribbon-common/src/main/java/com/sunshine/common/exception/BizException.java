package com.sunshine.common.exception;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/10 12:30
 **/
public class BizException extends BaseException{

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
        this.msg=message;
    }

    public BizException(String code, String msg) {
        super();
        this.msg=msg;
        this.code=code;
    }
}