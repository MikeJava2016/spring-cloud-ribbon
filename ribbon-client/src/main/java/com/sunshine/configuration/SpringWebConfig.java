package com.sunshine.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sunshine.annotation.SunShine;
import com.sunshine.mvc.returnValueHandler.ResultWarpReturnValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 先走filter -> Interceptor
 */
@Configuration
@EnableWebMvc
public class SpringWebConfig implements WebMvcConfigurer, InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/cors/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET","PUT","DELETE")
                .allowedOrigins("*");
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
        registry.addInterceptor(new SunShineHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/java");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new SunshineFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(-2);
        return bean;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> unmodifiableList = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new ResultWarpReturnValueHandler(returnValueHandler));
            } else {
                list.add(returnValueHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(list);
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
