package com.sunshine.utils.common.exception;

import com.sunshine.common.base.Result;
import com.sunshine.utils.common.ResultConstant;

import java.text.ParseException;

public class BaseException extends RuntimeException {
    private int code;

    private String errMessage;

    public BaseException(){
        code = ResultConstant.error.getCode();
        errMessage = ResultConstant.error.getMessage();
    }

    public BaseException(Result result){
        super(result.getMessage());
        this.code = result.getCode();
        this.errMessage = result.getMessage();
    }

    public BaseException(String errmsg){
        super(errmsg);
        this.code = ResultConstant.error.getCode();
        this.errMessage = errmsg;
    }

    public Result getResult(){
        return new Result().code(code).message(errMessage);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }


    public static Result getExceptionInfo(Throwable e){
        if( ParseException.class.isAssignableFrom(e.getClass()) ){
            return ResultConstant.error;
        }

        Throwable parentCause = e;
        Throwable cause = parentCause.getCause();
        while( null != cause ){
            parentCause = cause;
            cause = cause.getCause();
        }

        if(parentCause instanceof BaseException ){
            return ((BaseException) parentCause).getResult();
        }else {
            return ResultConstant.error;
        }
    }
}
