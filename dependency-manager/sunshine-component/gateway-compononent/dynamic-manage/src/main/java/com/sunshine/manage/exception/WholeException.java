package com.sunshine.manage.exception;

import com.sunshine.formwork.util.ApiResult;
import com.sunshine.formwork.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 捕获全局异常
 * @Author jianglong
 * @Date 2019/07/01
 * @Version V1.0
 */
@Slf4j
@Order(1)
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class WholeException {

    static{
        log.info("加载全局异常捕获类" );
    }

    /**
     * 返回异常包装信息
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public Object exceptionHandler(Exception e){
        log.error("error:",e);
        ApiResult result = new ApiResult(Constants.FAILED);
        result.setMsg(e.getMessage());
        return result;
    }
}
