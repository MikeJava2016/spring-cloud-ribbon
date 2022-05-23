package com.sunshine.annotation;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SunShine {


    String name() default "huzhanglin";

    boolean supported() default true;
}
