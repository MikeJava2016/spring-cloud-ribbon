package com.sunshine.configuration.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 先走filter -> Interceptor
 */
@Configuration
public class SpringWebConfig extends WebMvcConfigurationSupport implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;



    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/cors/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE")
                .allowedOrigins("*");
        super.addCorsMappings(registry);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        List<MediaType> mediaTypes = new ArrayList<>(16);
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        mediaTypes.add(MediaType.APPLICATION_CBOR);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        converter.setSupportedMediaTypes(mediaTypes);
        converters.add(converter);

        //创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }


    private final Logger logger = LoggerFactory.getLogger(getClass());

    //    addInterceptors：拦截器
    //  可以处理注解
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SunShineHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/hello");
        registry.addInterceptor(new SunShineHandlerInterceptor()).addPathPatterns("/hello/**");
    }





    @Override
    public void afterPropertiesSet() {
        // 处理HandlerMethodReturnValueHandler
        decorateReturnValueHandler();
     }

    private void decorateReturnValueHandler() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        this.decorateReturnValueHandler(handlers);
        adapter.setReturnValueHandlers(handlers);
    }

    private void decorateReturnValueHandler(List<HandlerMethodReturnValueHandler> handlers) {
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