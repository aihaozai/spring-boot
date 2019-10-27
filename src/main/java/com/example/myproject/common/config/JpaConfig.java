package com.example.myproject.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
//
//@Configuration
//public class JpaConfig {
//    @Configuration
//    @EnableTransactionManagement
//    @EnableJpaRepositories(basePackages = "com.example.myproject.common.baseDao",
//            entityManagerFactoryRef = "entityManagerFactoryBeanDefault",
//            transactionManagerRef = "platformTransactionManagerDefault")
//    public class JpaConfigDefault {
//        @Resource(name = "dsDefault")
//        DataSource dsDefault;
//        @Autowired
//        JpaProperties jpaProperties;
//
//        @Primary
//        @Bean(name = "entityManagerFactoryBeanDefault")
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBeanDefault(EntityManagerFactoryBuilder builder) {
//            return builder.dataSource(dsDefault)
//                    .properties(jpaProperties.getProperties())
//                    .packages("com.example.myproject.entity")
//                    .persistenceUnit("pu1")
//                    .build();
//        }
//        @Primary
//        @Bean(name = "platformTransactionManagerDefault")
//        PlatformTransactionManager platformTransactionManagerDefault(EntityManagerFactoryBuilder builder) {
//            LocalContainerEntityManagerFactoryBean factoryBeanDefault = entityManagerFactoryBeanDefault(builder);
//            return new JpaTransactionManager(factoryBeanDefault.getObject());
//        }
//    }
//
//    @Configuration
//    @EnableTransactionManagement
//    @EnableJpaRepositories(basePackages = "com.example.myproject.common.baseDao",
//            entityManagerFactoryRef = "entityManagerFactoryBeanPrimary",
//            transactionManagerRef = "platformTransactionManagerPrimary")
//    public class JpaConfigPrimary {
//        @Resource(name = "dsPrimary")
//        DataSource dsPrimary;
//        @Autowired
//        JpaProperties jpaProperties;
//
//        @Bean(name = "entityManagerFactoryBeanPrimary")
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBeanPrimary(EntityManagerFactoryBuilder builder) {
//            return builder.dataSource(dsPrimary)
//                    .properties(jpaProperties.getProperties())
//                    .packages("com.example.myproject.entity")
//                    .persistenceUnit("pu2")
//                    .build();
//        }
//        @Bean(name = "platformTransactionManagerPrimary")
//        PlatformTransactionManager platformTransactionManagerPrimary(EntityManagerFactoryBuilder builder) {
//            LocalContainerEntityManagerFactoryBean factoryBeanPrimary = entityManagerFactoryBeanPrimary(builder);
//            return new JpaTransactionManager(factoryBeanPrimary.getObject());
//        }
//    }
//
//    @Configuration
//    @EnableTransactionManagement
//    @EnableJpaRepositories(basePackages = "com.example.myproject.common.baseDao",
//            entityManagerFactoryRef = "entityManagerFactoryBeanSecondary",
//            transactionManagerRef = "platformTransactionManagerSecondary")
//    public class JpaConfigSecondary{
//        @Resource(name = "dsSecondary")
//        DataSource dsSecondary;
//        @Autowired
//        JpaProperties jpaProperties;
//        @Bean("entityManagerFactoryBeanSecondary")
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBeanSecondary(EntityManagerFactoryBuilder builder){
//            return builder.dataSource(dsSecondary)
//                    .properties(jpaProperties.getProperties())
//                    .packages("com.example.myproject.entity")
//                    .persistenceUnit("pu3")
//                    .build();
//        }
//
//        @Bean("platformTransactionManagerSecondary")
//        PlatformTransactionManager platformTransactionManagerSecondary(EntityManagerFactoryBuilder builder){
//            LocalContainerEntityManagerFactoryBean factoryBeanSecondary = entityManagerFactoryBeanSecondary(builder);
//            return new JpaTransactionManager(factoryBeanSecondary.getObject());
//        }
//    }
//}
   

