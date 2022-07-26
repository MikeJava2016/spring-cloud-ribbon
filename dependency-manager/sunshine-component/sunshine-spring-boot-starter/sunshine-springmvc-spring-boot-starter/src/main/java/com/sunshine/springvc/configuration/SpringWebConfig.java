package com.sunshine.springvc.configuration;


import com.sunshine.common.base.Result;
import com.sunshine.common.util.ManagerTokenUtil;
import com.sunshine.springvc.filter.ReadRequestBodyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

import javax.servlet.MultipartConfigElement;
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

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new ReadRequestBodyFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(-2);
        return bean;
    }


    /**
     * 文件上传配置
     *
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(20));
        // Sets the directory location where files will be stored.
        // factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }
}
