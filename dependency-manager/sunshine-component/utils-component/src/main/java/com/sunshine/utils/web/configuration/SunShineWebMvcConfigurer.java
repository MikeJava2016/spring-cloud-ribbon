package com.sunshine.utils.web.configuration;

import com.sunshine.utils.common.Result;
import com.sunshine.utils.web.ResultWarpReturnValueHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

@Slf4j
public class SunShineWebMvcConfigurer implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                log.info(" class  = {} , preHandle",SunShineWebMvcConfigurer.class.getName());
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                log.info(" class  = {} , postHandle",SunShineWebMvcConfigurer.class.getName());
                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                log.info(" class  = {} , afterCompletion",SunShineWebMvcConfigurer.class.getName());
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            }
        });
    }

    // 添加异步回调拦截器
    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60 * 1000L);
        configurer.registerCallableInterceptors(timeoutInterceptor());
        configurer.registerCallableInterceptors(returnValueHandlers());
        configurer.setTaskExecutor(threadPoolTaskExecutor());
        log.info("配置异步");
    }

    @Bean
    private CallableProcessingInterceptor returnValueHandlers() {
        log.info("SunShineWebMvcConfigurer CallableProcessingInterceptor");
        CallableProcessingInterceptor processingInterceptor = new CallableProcessingInterceptor() {

            @Override
            public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
                log.info("returnValueHandlers postProcess");
                if (concurrentResult instanceof Result) {
                    return;
                }
                log.info("returnValueHandlers postProcess 3");
                ResultWarpReturnValueHandler.convertReturnValue(concurrentResult);
            }


            @Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                return Result.fail("处理超时");
            }

            @Override
            public <T> Object handleError(NativeWebRequest request, Callable<T> task, Throwable t) throws Exception {
                t.printStackTrace();
                return Result.fail(t.getMessage());
            }
        };
        return processingInterceptor;

    }

    @Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setThreadNamePrefix("controller thread");
        log.info("SunShineWebMvcConfigurer threadPoolTaskExecutor...");
        return executor;
    }


}
