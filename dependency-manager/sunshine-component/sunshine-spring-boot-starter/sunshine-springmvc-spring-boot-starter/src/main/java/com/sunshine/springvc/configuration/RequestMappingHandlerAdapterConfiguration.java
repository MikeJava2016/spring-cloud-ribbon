package com.sunshine.springvc.configuration;

import com.sunshine.springvc.ResultWarpReturnValueHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class RequestMappingHandlerAdapterConfiguration implements InitializingBean {
    @Autowired
    private RequestMappingHandlerAdapter adapter;


    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有的返回值处理器
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        // 自定义的方法参数返回值解析器
        List<HandlerMethodReturnValueHandler> customReturnValueHandlers = adapter.getCustomReturnValueHandlers();
        // 参数解析器
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
        for (HandlerMethodArgumentResolver argumentResolver : argumentResolvers) {
            argumentResolver = decorateHandlerMethodArgumentResolver(argumentResolver);

        }

        // 自定义参数参数解析器
        List<HandlerMethodArgumentResolver> customArgumentResolvers = adapter.getCustomArgumentResolvers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        this.decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
        log.info(" RequestMappingHandlerAdapterConfiguration...");
    }

    private HandlerMethodArgumentResolver decorateHandlerMethodArgumentResolver(HandlerMethodArgumentResolver argumentResolver) {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return argumentResolver.supportsParameter(parameter);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                // 这里可以做参数校验
                // 判断获取
                HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
                String method = request.getMethod();
                log.info("decorateHandlerMethodArgumentResolver method = {} ",method);
                return argumentResolver.resolveArgument(parameter,mavContainer,webRequest,binderFactory);
            }
        };
    }


    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                ResultWarpReturnValueHandler decorator = new ResultWarpReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }



}
