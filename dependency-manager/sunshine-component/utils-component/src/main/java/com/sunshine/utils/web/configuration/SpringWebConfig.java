package com.sunshine.utils.web.configuration;


import com.sunshine.common.base.Result;
import com.sunshine.common.util.ManagerTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 先走filter -> Interceptor
 */
@Slf4j
public class SpringWebConfig  {

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public Result handleException(BindException e, HttpServletRequest request) {
            List<FieldError> fieldErrors = e.getFieldErrors();
            StringBuilder message = new StringBuilder();
            fieldErrors.forEach(
                    fieldError -> message.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",")
            );
            log.info("GlobalExceptionHandler.handleException:{},{}", request.getRequestURI(), message);
            ManagerTokenUtil.removeUid();
            return Result.fail(message.substring(0, message.length() - 1));
        }
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        log.info("初始化线程池 taskExecutor");
        return executor;
    }

    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(2500).setTaskExecutor(new ConcurrentTaskExecutor())
                .registerCallableInterceptors(new CallableProcessingInterceptor() { })
                .registerDeferredResultInterceptors(new DeferredResultProcessingInterceptor() {});
    }

}
