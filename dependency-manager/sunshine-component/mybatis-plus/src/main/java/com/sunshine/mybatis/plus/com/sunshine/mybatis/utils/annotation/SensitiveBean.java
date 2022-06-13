package com.sunshine.mybatis.plus.com.sunshine.mybatis.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * https://www.codeleading.com/article/98311634475/
 */
@Target({ElementType.TYPE})//该参数代表是作用在类、方法上的
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveBean {
    String value() default "";
}
