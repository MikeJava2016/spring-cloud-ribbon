/*
package com.sunshine.security.config.SecurityConfigurerAdapter;

import com.sunshine.security.config.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

*
 * @version v1
 * @Description 注解相关  功能没有实现
 * @Author huzhanglin
 * @Date 2022/5/22 10:08
 *

//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(0)
public class AnnotationWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

*
     * 在这里使用Spring为我们提供的RequestMappingHandlerMapping类，
     * 我们可以通过requestMappingHandlerMapping.getHandlerMethods();获取到所有的RequestMappingInfo信息。


    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

*
     * @ description: 使用这种方式放行的接口，不走 Spring Security 过滤器链，
     * 无法通过 SecurityContextHolder 获取到登录用户信息的，
     * 因为它一开始没经过 SecurityContextPersistenceFilter 过滤器链。


    @Override
    public void configure(WebSecurity web) throws Exception {
        WebSecurity and = web.ignoring().and();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        handlerMethods.forEach((requestMappingInfo, method) -> {
            // 带IgnoreAuth注解的方法直接放行
            if (null != (method.getMethodAnnotation(IgnoreAuth.class))) {
                // 根据请求类型做不同的处理
                requestMappingInfo.getMethodsCondition().getMethods().forEach(requestMethod -> {
                    switch (requestMethod) {
                        case GET:
                            // getPatternsCondition得到请求url数组，遍历处理
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                // 放行
                                and.ignoring().antMatchers(HttpMethod.GET, pattern);
                            });
                            break;
                        case POST:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.POST, pattern);
                            });
                            break;
                        case DELETE:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.DELETE, pattern);
                            });
                            break;
                        case PUT:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.PUT, pattern);
                            });
                            break;
                        default:
                            break;
                    }
                });
            }
        });
    }
}
*/
