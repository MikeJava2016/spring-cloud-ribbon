package com.sunshine.utils.common;

import com.sunshine.common.base.Result;
import com.sunshine.utils.common.exception.BaseException;

public class BusinessException extends BaseException {

    private String errDesc;

    public BusinessException(){

    }

    public BusinessException(Result result){
        super(result);
    }

    public BusinessException(Result result, String errDesc){
        super(result);
        this.errDesc = errDesc;
    }

    public BusinessException(String errDesc){
        super(errDesc);
        this.errDesc = errDesc;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
