package com.example.myproject.common.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.example.myproject.common.DataSource.DynamicDataSource;
import com.example.myproject.common.DataSource.DynamicDataSourceContextHolder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//public class DataSourceConfig {
//    /*
//    *多数据源
//    *jdbc和mybatis配置一致
//     */
////    @Bean("dsDefault")
////    @Primary
////    @ConfigurationProperties("spring.datasource")
////    DataSource dsDefault(){
////        return DruidDataSourceBuilder.create().build();
////    }
////
////    @Bean("dsPrimary")
////    @ConfigurationProperties("spring.datasource.primary")
////    DataSource dsPrimary(){
////        return DruidDataSourceBuilder.create().build();
////    }
////
////    @Bean("dsSecondary")
////    @ConfigurationProperties("spring.datasource.secondary")
////    DataSource dsSecondary(){
////        return DruidDataSourceBuilder.create().build();
////    }
//
////    @Bean("dynamicDataSource")
////    public DataSource dynamicDataSource(){
////        DynamicDataSource dynamicDataSource = new DynamicDataSource();
////        // 默认数据源
////        dynamicDataSource.setDefaultTargetDataSource(dsDefault());
////        // 配置多数据源
////        Map<Object, Object> dsMap = new HashMap();
////        dsMap.put("dsDefault", dsDefault());
////        dsMap.put("dsPrimary", dsPrimary());
////        dsMap.put("dsSecondary", dsSecondary());
////        dynamicDataSource.setTargetDataSources(dsMap);
////        DynamicDataSourceContextHolder.addDataSourceType(dsMap.keySet());
////        return dynamicDataSource;
////    }
////    @Bean(name="sqlSessionFactory")
////    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource")DataSource dataSource) throws Exception {
////        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
////        bean.setDataSource(dataSource);
////        return bean.getObject();
////    }
////    @Bean(name="platformTransactionManager")//事务管理@Transactional(TransactionManager="mysqlTransactionManager")
////    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource")DataSource dataSource) {
////        return new DataSourceTransactionManager(dataSource);
////    }
////    @Bean(name="sqlSessionTemplate")
////    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory")SqlSessionFactory sqlSessionFactory) throws Exception {
////        return new SqlSessionTemplate(sqlSessionFactory);
////    }
//}
