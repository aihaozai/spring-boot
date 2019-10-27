package com.example.myproject.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*@Configuration
public class MyBatisConfig {
    @MapperScan(value="com.example.myproject.mapper",sqlSessionFactoryRef = "sqlSessionFactoryBean")
    public class MyBatisConfigOne{
        @Autowired
        @Qualifier("dsPrimary")
        DataSource dsPrimary;
        @Bean
        SqlSessionFactory sqlSessionFactoryBean() throws Exception{
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dsPrimary);
            return factoryBean.getObject();
        }

        @Bean
        SqlSessionTemplate sqlSessionTemplate1() throws Exception {
            return new SqlSessionTemplate(sqlSessionFactoryBean());
        }
    }

    @MapperScan(value="com.example.myproject.mapper1",sqlSessionFactoryRef = "sqlSessionFactoryBean1")
    public class MyBatisConfigTwo{
        @Autowired
        @Qualifier("dsSecondary")
        DataSource dsSecondary;
        @Bean
        SqlSessionFactory sqlSessionFactoryBean1() throws Exception{
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dsSecondary);
            return factoryBean.getObject();
        }
        @Bean
        SqlSessionTemplate sqlSessionTemplate2() throws Exception {
            return new SqlSessionTemplate(sqlSessionFactoryBean1());
        }
    }
}*/
