package com.example.myproject.common.annotation;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-08-12 15:59
 **/

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String[] value() default{};
}
