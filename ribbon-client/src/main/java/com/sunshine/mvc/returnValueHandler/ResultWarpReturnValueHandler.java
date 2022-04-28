package com.sunshine.mvc.returnValueHandler;

import annotation.JsonEncrypt;
import com.sunshine.entity.Result;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import utils.EncryptViewUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class ResultWarpReturnValueHandler  implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ResultWarpReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        delegate.handleReturnValue(convertReturnValue(returnValue), returnType, mavContainer, webRequest);
    }

    private Object convertReturnValue(Object source) {
        if (null != source) {
            jsonEncrypt(source);
        }
        return source;
    }

    private void jsonEncrypt(Object source) {
        if(source instanceof List) {
            Iterable iterable = (Iterable) source;
            for (Object object : iterable) {
                jsonEncrypt(object);
            }
        } else if(source instanceof Result) {
            Result<?> result = (Result<?>) source;
            Object data = result.getData();
            if(null != data) {
                 jsonEncrypt(data);
            }
        } else {
            doJsonEncrypt(source);
        }
    }

    private void doJsonEncrypt(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        if(ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                // 处理多个注解
                JsonEncrypt jsonEncrypt = field.getAnnotation(JsonEncrypt.class);
                if(null != jsonEncrypt) {
                    doJsonEncrypt(field, jsonEncrypt, object);
                }
            }
        }
    }

    private void doJsonEncrypt(Field field, JsonEncrypt jsonEncrypt, Object object) {
        try {
            field.setAccessible(true);
            Object val = field.get(object);
            if(null != val) {
                // 加密值
                String strVal = String.valueOf(val);

                // 加密参数
                String spanChar = jsonEncrypt.value();
                int beginIdx = jsonEncrypt.beginIdx(), endIdx = jsonEncrypt.endIdx();

                // 设置加密后的值
                field.set(object, EncryptViewUtils.toEncryptView(strVal, spanChar, beginIdx, endIdx));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}