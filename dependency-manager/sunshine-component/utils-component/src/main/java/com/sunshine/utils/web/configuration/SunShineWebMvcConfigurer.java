package com.sunshine.utils.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
