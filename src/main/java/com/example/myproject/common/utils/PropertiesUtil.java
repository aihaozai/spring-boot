package com.example.myproject.common.utils;

import org.springframework.core.env.Environment;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-14 14:09
 **/
public class PropertiesUtil {

    private static Environment env = null;

    public static void setEnvironment(Environment env) {
        PropertiesUtil.env = env;
    }
    public static String getProperty(String key) {
        return PropertiesUtil.env.getProperty(key);
    }


}
