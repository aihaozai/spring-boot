package com.example.myproject.common.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalConfig {
    @ModelAttribute(value = "info")
    public Map<String,String>userInfo () {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", "111");
        map.put("phone", "222");
        return map;
    }
}
