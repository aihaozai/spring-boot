package com.example.myproject.common.config;


import com.example.myproject.common.utils.PropertiesUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-14 14:08
 **/

@Configuration
@PropertySource("classpath:system.properties")
public class PropertiesConfig {
    @Resource
    private Environment env;
    @PostConstruct
    public void setProperties() {
        PropertiesUtil.setEnvironment(env);
    }

}
