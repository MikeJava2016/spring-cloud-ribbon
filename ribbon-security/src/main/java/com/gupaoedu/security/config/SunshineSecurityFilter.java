package com.gupaoedu.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/17 20:28
 **/
public class SunshineSecurityFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestId = httpRequest.getHeader("Request-Id");
        logger.info("SunshineSecurityFilter start requestId =  {}" ,requestId);
        requestId = UUID.randomUUID().toString();
        filterChain.doFilter(request, response);
        logger.info("SunshineSecurityFilter end requestId =  {}" ,requestId);
    }


}
