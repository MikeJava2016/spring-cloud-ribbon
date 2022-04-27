package com.sunshine.configuration;

import com.sunshine.annotation.SunShine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * 先走filter -> Interceptor
 */
@Configuration
@EnableWebMvc
public class SpringWebConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

//    addInterceptors：拦截器
    //  可以处理注解
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SunShineHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/java");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SunshineFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(20);
        return bean;
    }

    /**
     * 只是加载一次
     */
    class SunshineFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            String filterName = filterConfig.getFilterName();
            Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
            logger.info("filterConfig = > {}",filterConfig);
        }

        @Override
        public void destroy() {

        }

        /**
         * 这个方法会执行多次  每次请求都调用一次
         * @param servletRequest
         * @param servletResponse
         * @param filterChain
         * @throws IOException
         * @throws ServletException
         */
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            logger.info("filter");
            String requestURI = request.getRequestURI();
            logger.info("requestURI = {}",requestURI);
            filterChain.doFilter(request, response);
        }
    }
    class SunShineHandlerInterceptor implements HandlerInterceptor{
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
    }

}
