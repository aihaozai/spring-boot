package com.example.myproject.common.config;

import com.example.myproject.common.shiro.ShiroProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import lombok.Getter;
/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-17 10:29
 **/
@Component
@Getter
@PropertySource(value = {"classpath:system.properties"})
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    private ShiroProperties shiro = new ShiroProperties();
}
