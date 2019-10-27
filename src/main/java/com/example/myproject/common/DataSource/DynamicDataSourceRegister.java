package com.example.myproject.common.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * @Program: myproject
 * @Point 动态数据源注册
 * @Point 启动动态数据源请在启动类中 添加 @Import(DynamicDataSourceRegister.class)
 * @Author: haozai
 * @Create: 2019-10-08 14:24
 **/

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";

    // 数据源
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<>();

    private static String DB_NAME = "names";
    private static String DB_DEFAULT_VALUE = "spring.datasource";

    /**
     * 加载多数据源配置
     */

    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initCustomDataSources(env);
    }

    /**
     * 2.0.4 初始化主数据源
     */

    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
        logger.info("初始化主数据源:{}",env.getProperty(DB_DEFAULT_VALUE +"." +"defaultName"));
        defaultDataSource = buildDataSource(dsMap(env,null));
    }



    // 初始化更多数据源
    private void initCustomDataSources(Environment env) {
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        //RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env,DB_DEFAULT_VALUE+".");
        String dsPrefixs = env.getProperty(DB_DEFAULT_VALUE + "." + DB_NAME);
        assert dsPrefixs != null;
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            DataSource ds = buildDataSource(dsMap(env,dsPrefix));
            customDataSources.put(dsPrefix, ds);
        }
    }

    private Map<String, Object> dsMap(Environment env,String value){
       if(value==null) value="";
       else {logger.info("初始化数据源:{}",value); value += "."; }
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", env.getProperty(DB_DEFAULT_VALUE + "." + value+ "type"));
        dsMap.put("driver-class-name", env.getProperty(DB_DEFAULT_VALUE + "." + value+ "driver-class-name"));
        dsMap.put("url", env.getProperty(DB_DEFAULT_VALUE + "." + value+ "url"));
        dsMap.put("username", env.getProperty(DB_DEFAULT_VALUE + "." + value+ "username"));
        dsMap.put("password", env.getProperty(DB_DEFAULT_VALUE + "." + value+ "password"));
        return dsMap;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        DynamicDataSourceContextHolder.dataSourceList.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
        logger.info("Dynamic DataSource Registry");
    }

    // 创建DataSource
    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null)
                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
