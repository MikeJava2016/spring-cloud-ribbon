package annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonEncrypt {
    String value() default "*";

    int beginIdx() default 0;

    int endIdx() default 0;
}
