package com.sunshine.security.web;

import com.sunshine.security.controller.HelloController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/6/4 17:42
 **/
@Configuration
public class WebCommonConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public void setHandlerMapping(RequestMappingHandlerMapping mapping, HelloController handler)
            throws NoSuchMethodException {
        logger.info("手动初始化 handlerMapping【start】...");
        RequestMappingInfo info = RequestMappingInfo
                .paths("/hello2").methods(RequestMethod.GET).build();
        Method method = HelloController.class.getMethod("hello");
        mapping.registerMapping(info, handler, method);
        logger.info("手动初始化 handlerMapping【end】...");
    }
}
