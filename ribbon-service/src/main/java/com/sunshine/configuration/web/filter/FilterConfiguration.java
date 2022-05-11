package com.sunshine.configuration.web.filter;

import com.sunshine.common.util.ManagerTokenUtil;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Configuration
public class FilterConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SunshineFilter());
        bean.setName("SunshineFilter");//过滤器名称
        bean.addUrlPatterns("/*");//过滤所有路径
        bean.setOrder(1);//优先级，越低越优先
        bean.addInitParameter("excludedUris", "/login,/token");//需要排除的uri
        bean.addUrlPatterns("/*");
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
            logger.info("filterConfig = > {}", filterConfig);
        }

        @Override
        public void destroy() {

        }

        /**
         * 这个方法会执行多次  每次请求都调用一次
         *
         * @param servletRequest
         * @param servletResponse
         * @param filterChain
         * @throws IOException
         * @throws ServletException
         */
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            // 从header中取数据
            String uid = request.getHeader("uid");
            if (uid != null) {
                logger.info(" token = {}", uid);
            }
            logger.info("filter");
            String requestURI = request.getRequestURI();
            logger.info("requestURI = {}", requestURI);
            String method = request.getMethod();
//            Object token = request.getAttribute("token");
            ManagerTokenUtil.setUid(uid);
            filterChain.doFilter(request, response);
            //
            ManagerTokenUtil.removeUid();
        }
    }

}
