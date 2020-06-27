package com.example.myproject.common.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatedRequests {
    /**
     * 接口限制时间
     * @return
     */
    long value() default 1000;
}
