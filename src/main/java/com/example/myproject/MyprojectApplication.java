package com.example.myproject;

import com.example.myproject.common.DataSource.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableCaching
@SpringBootApplication(scanBasePackages="com.example.myproject.*")    //exclude = {DataSourceAutoConfiguration.class} 禁用数据源自动配置
@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源*/
@MapperScan({"com.example.myproject.mapper","com.example.myproject.mapper1"})
public class MyprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyprojectApplication.class, args);
    }

}
