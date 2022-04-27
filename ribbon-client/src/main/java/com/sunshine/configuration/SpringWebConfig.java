package com.sunshine.configuration;

import com.sunshine.annotation.SunShine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Configuration
@EnableWebMvc
public class SpringWebConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

//    addInterceptors：拦截器
    //  可以处理注解
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor(){
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                logger.info("preHandle1");
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation  declaredAnnotation : declaredAnnotations) {
                    this.handleAnnotations(handlerMethod,declaredAnnotation,request,response);
                }
                return true;
            }

            /**
             * 处理注解
             * @param declaredAnnotation
             * @param request
             * @param response
             */
            private void handleAnnotations(HandlerMethod handlerMethod, Annotation declaredAnnotation, HttpServletRequest request, HttpServletResponse response) {
                logger.info("method ={}.",handlerMethod.getMethod().toGenericString());
                if(declaredAnnotation instanceof SunShine){
                    SunShine sunShine = (SunShine) declaredAnnotation;
                    String name = sunShine.name();
                    logger.info(" name = {}",name);
                }
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                logger.info("postHandle");
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                logger.info("afterCompletion");
            }
        }).addPathPatterns("/**");

    }

//    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest)servletRequest;
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                logger.info("filter");
                filterChain.doFilter(request, response);
            }
        });
        bean.addUrlPatterns("/**");
        return bean;
    }


//    @Bean
    public WebMvcConfigurationSupport a(){
        return new WebMvcConfigurationSupport(){
            @Override
            protected void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor(){
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        logger.info("preHandle123");
                        return false;
                    }

                    @Override
                    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                        logger.info("postHandle123");
                    }

                    @Override
                    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                        logger.info("afterCompletion 123");

                    }
                });
                super.addInterceptors(registry);
            }
        };
    }


}
