package com.sunshine.springvc.configuration;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class BaseHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public BaseHandlerMethodArgumentResolver() {

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

}
