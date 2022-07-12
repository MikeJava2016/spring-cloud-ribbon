package com.sunshine.springvc.inteceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/11 下午 04:25
 **/
public class SunshineInterceptor  implements HandlerInterceptor {

    private  WebApplicationContext applicationContext;

    private final Object lock = new Object();

    private static final Logger logger = LoggerFactory.getLogger(SunshineInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initBean(request);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void initBean(HttpServletRequest request) {
        if (null == applicationContext) {
            synchronized (lock) {
                if (null == applicationContext) {
                      applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                }
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
