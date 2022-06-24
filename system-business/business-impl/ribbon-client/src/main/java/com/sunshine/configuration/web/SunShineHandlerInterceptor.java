package com.sunshine.configuration.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunshine.annotation.SunShine;
import com.sunshine.common.annontation.Validate;
import com.sunshine.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

public class SunShineHandlerInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> String validate(T object) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(object);
        if (violationSet != null && !CollectionUtils.isEmpty(violationSet)) {
            Iterator<ConstraintViolation<T>> iterator = violationSet.iterator();
            StringBuffer sb = new StringBuffer(64);
            while (iterator.hasNext()) {
                sb.append(iterator.next().getMessage()).append(";");
            }
            return sb.toString();
        }
        return null;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       /* HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        logger.info("preHandle1");
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        boolean preHandle ;
        // @RequestBody @Validate
        for (Annotation declaredAnnotation : handlerMethod.getMethod().getParameterAnnotations()[0]) {
            preHandle = this.handleAnnotations(handlerMethod, declaredAnnotation, request, response);
            if (!preHandle) {
                return false;
            }
            //handlerMethod.getMethod().getParameterAnnotations()
        }*/
        return true;
    }

    /**
     * 处理注解
     *
     * @param declaredAnnotation
     * @param request
     * @param response
     */
    private boolean handleAnnotations(HandlerMethod handlerMethod, Annotation declaredAnnotation, HttpServletRequest request, HttpServletResponse response) {
        logger.info("method ={}.", handlerMethod.getMethod().toGenericString());
        if (declaredAnnotation instanceof SunShine) {
            this.handleAnnotationSunShine((SunShine) declaredAnnotation);
            return true;
        }

        return true;
    }

    private void handleError(HttpServletResponse response, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String rs = "";
            try {
                rs = objectMapper.writeValueAsString(Result.fail(message));
            } catch (JsonProcessingException e) {

            }
            writer.write(rs);
            writer.flush();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    private String handleAnnotationValidate(Validate declaredAnnotation, MethodParameter[] methodParameters) {
        MethodParameter methodParameter = methodParameters[0];
        String message = validate(methodParameter);
        return message;
    }

    private void handleAnnotationSunShine(SunShine sunShine) {
        String name = sunShine.name();
        logger.info(" name = {}", name);
        boolean supported = sunShine.supported();
        logger.info(" supported = {}", supported);
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


