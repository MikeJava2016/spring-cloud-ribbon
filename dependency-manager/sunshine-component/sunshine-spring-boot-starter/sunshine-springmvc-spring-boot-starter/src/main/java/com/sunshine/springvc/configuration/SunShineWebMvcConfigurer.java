package com.sunshine.springvc.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sunshine.common.base.Result;
import com.sunshine.springvc.ResultWarpReturnValueHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class SunShineWebMvcConfigurer implements WebMvcConfigurer , InitializingBean {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new BaseHandlerMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    // 添加转化器
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
        log.info("configureMessageConverters");
    }

    // 添加跨域处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE")
                .allowedOrigins("*");
        log.info("addCorsMappings");
    }

    // 包装返回值处理器  此处可以添加返回值处理器
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        log.info("addReturnValueHandlers0");
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                log.info("addReturnValueHandlers1");
                HandlerMethodReturnValueHandler decorator = new ResultWarpReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }

        log.info("addReturnValueHandlers");
    }

    @Override
    // 添加拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 心跳检测需要跳过
                log.info(" class  = {} , preHandle",SunShineWebMvcConfigurer.class.getName());
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                log.info(" class  = {} , postHandle",SunShineWebMvcConfigurer.class.getName());
                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                log.info(" class  = {} , afterCompletion",SunShineWebMvcConfigurer.class.getName());
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
            }
        });
//        registry.addWebRequestInterceptor()
    }

    // 添加异步回调拦截器
    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60 * 1000L);
        configurer.registerCallableInterceptors(timeoutInterceptor());
        configurer.registerCallableInterceptors(returnValueHandlers());
        configurer.setTaskExecutor(threadPoolTaskExecutor());
        log.info("配置异步");
    }

    @Bean
    private CallableProcessingInterceptor returnValueHandlers() {
        log.info("SunShineWebMvcConfigurer CallableProcessingInterceptor");
        CallableProcessingInterceptor processingInterceptor = new CallableProcessingInterceptor() {

            @Override
            public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
                log.info("returnValueHandlers postProcess");
                if (concurrentResult instanceof Result) {
                    return;
                }
                log.info("returnValueHandlers postProcess 3");
                ResultWarpReturnValueHandler.convertReturnValue(concurrentResult);
            }


            @Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                return Result.fail("处理超时");
            }

            @Override
            public <T> Object handleError(NativeWebRequest request, Callable<T> task, Throwable t) throws Exception {
                t.printStackTrace();
                return Result.fail(t.getMessage());
            }
        };
        return processingInterceptor;

    }

    @Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }

    // 线程池配置
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setThreadNamePrefix("controller thread");
        log.info("SunShineWebMvcConfigurer threadPoolTaskExecutor...");
        return executor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public RequestMappingHandlerAdapterConfiguration requestMappingHandlerAdapterConfiguration(){
        return new RequestMappingHandlerAdapterConfiguration();
    }


}
