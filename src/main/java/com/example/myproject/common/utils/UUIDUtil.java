package com.example.myproject.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-25 10:58
 **/
public class UUIDUtil {
    protected UUIDUtil(){};
    public static String randomUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
    public static String currentTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
