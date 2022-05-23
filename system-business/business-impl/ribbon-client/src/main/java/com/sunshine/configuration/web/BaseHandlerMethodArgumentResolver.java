package com.sunshine.configuration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.util.List;

public class BaseHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public BaseHandlerMethodArgumentResolver(Validator globalValidator) {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
//        return  parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object object = request.getAttribute("token");
        if (object == null){
            return null;
        }
        //  根据token返回userinfo
       // User user =  this.getByToken((String)object);
       // ManagerTokenUtil.setThreadToken(object.toString());

        return object;
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Autowired
        private javax.validation.Validator globalValidator;

        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new BaseHandlerMethodArgumentResolver(globalValidator));

        }

    }
}
