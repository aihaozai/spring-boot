package com.example.myproject.common.DataSource;

import javax.sql.DataSource;
import java.util.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-08 10:54
 **/

public class DynamicDataSourceContextHolder{
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static List<Object> dataSourceIds = new ArrayList<>();
    public static Map<String, DataSource> dataSourceList = new HashMap<>();

    /**
     * 设置数据源
     */
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    /**
     * 获取数据源
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    /**
     * 判断指定DataSrouce当前是否存在
     */
    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
    /**
     * 添加数据源
     */
    public static boolean addDataSourceType(Collection<? extends Object> keys) {
        return dataSourceIds.addAll(keys);
    }
}

