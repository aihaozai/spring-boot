package com.example.myproject.common.utils;

import com.example.myproject.service.sys.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-27 10:26
 **/
@Component
public class TestUtils {

    @Autowired
    private IMenuService iMenuService;
    public static TestUtils testUtils;

    @PostConstruct
    public void init() {
        testUtils = this;
    }

    //utils工具类中使用service和mapper接口的方法例子，用"testUtils.xxx.方法" 就可以了
//    public static void test(){
//        testUtils.iMenuService.findListByPage();
//    }

}
