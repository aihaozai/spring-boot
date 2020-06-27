package com.example.myproject.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-04 10:30
 **/
public class SystemStringUtil {
    protected SystemStringUtil(){};
    public static String isEmpty(String target){
        if(StringUtils.isEmpty(target))return  "";
        else return target;
    }
}
