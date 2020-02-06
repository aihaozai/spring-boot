package com.example.myproject.common.baseDao;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.cglib.beans.BeanMap;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-08-28 17:57
 **/
@Transactional
public class BaseDao<T,ID extends Serializable> {

    private Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    //很重要，映射entityManager
    protected BaseDao(){
        Type genType = getClass().getGenericSuperclass();//通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();//返回参数数组
        entityClass=(Class)params[0];
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    public void save(T entity){
        this.entityManager.persist(entity);
        this.entityManager.flush();
    }
    public void  update(ID id){
        T entity = findByID(id);
        this.entityManager.merge(entity);
        this.entityManager.flush();
    }
    public void  update(T entity){
        this.entityManager.merge(entity);
        this.entityManager.flush();
    }

    public void delete(ID id){
        T entity = findByID(id);
        this.entityManager.remove(entity);
    }

    public T findByID(ID id){
        T pojo = (T) this.entityManager.find(entityClass,id);
        return pojo;
    }

    public Integer deleteByFiled(String field,Object object){
        String hsql = MessageFormat.format("delete {0} where {1} = ''{2}'' ", entityClass.getSimpleName(),field,object);
        return this.entityManager.createQuery(hsql).executeUpdate();
    }
    public Integer deleteByFiled(String tField,Object tObject,String nField,Object object){
        String hsql = MessageFormat.format("delete {0} where {1} = ''{2}'' and {3} = ''{4}'' ", entityClass.getSimpleName(),tField,tObject,nField,object);
        return this.entityManager.createQuery(hsql).executeUpdate();
    }
    /**
     *@author Jen
     *@Point 更新多个字段
     *@Param  file 字段名 object值 filed根据字段 object值
     *@return Bean
     */
    public Integer updateById(String id,HashMap<String,Object> map){
        String primary = getEntityPrimary(entityClass);
        String sql = getSetSql(map);
        String hsql = MessageFormat.format("update {0} {1} where {2} = ''{3}'' ", entityClass.getSimpleName(),sql,primary,id);
        return this.entityManager.createQuery(hsql).executeUpdate();
    }
    /**
     *@author Jen
     *@Point 更新一个字段
     *@Param  file 字段名 object值 filed根据字段 object值
     *@return Bean
     */
    public Integer updateByFiled(String tField,Object tObject,String filed,Object object){
        String hsql = MessageFormat.format("update {0} set {1} = ''{2}'' where {3} = ''{4}'' ", entityClass.getSimpleName(),tField,tObject,filed,object);
        return this.entityManager.createQuery(hsql).executeUpdate();
    }
    /**
     *@author Jen
     *@Point 根据主键更新一个字段
     *@Param  file 字段名 object值
     *@return Bean
     */
    public Integer updateById(String id,String field,Object object) throws Exception{
        String primary = getEntityPrimary(entityClass);
        if(StringUtils.isNotEmpty(primary)){
            String hsql = MessageFormat.format("update {0} set {1} = ''{2}'' where {3} = ''{4}'' ", entityClass.getSimpleName(),field,object,primary,id);
            return this.entityManager.createQuery(hsql).executeUpdate();
        }else throw new Exception("没有找到实体类主键");
    }

    public List<T> findAll(String hsql){
        Query query = this.entityManager.createQuery(hsql);
        return query.getResultList();
    }
    public Object findByHSQL(String hsql){
        Query query = this.entityManager.createQuery(hsql);
        return query.getSingleResult();
    }
    public List<T> findAll() {
        return this.entityManager.createQuery("FROM "+entityClass.getSimpleName()).getResultList();
    }
    /**
     *@author Jen
     *@Param entity 实体类 file 字段名 object查询参数
     *@return Bean
     */
    public <T> T findSingleBeanByEntitySql(Class entityClass,String filed, Object o)throws Exception{
        Class<?> cls = entityClass;
        String tableName = cls.getAnnotation(Table.class).name();
        String sql = MessageFormat.format("SELECT * FROM {0} where {1} = ''{2}''", tableName, filed, o);
        HashMap map = getMapBySQLQuery(sql);
        entityManager.close();
        return (T)MapToBean(map,entityClass);
    }
    /**
     *@author Jen
     *@Point 根据一个字段查询HSQL
     *@Param file 字段名 object查询参数
     *@return Bean
     */
    public <T> T findSingleBeanByFiled(String filed, Object o){
        String sql = MessageFormat.format("FROM {0} where {1} = ''{2}''", entityClass.getSimpleName(), filed, o);
        T bean = createHSQLSingle(sql);
        entityManager.close();
        return bean;
    }

    /**
     *@author Jen
     *@Point 根据一个字段查询数量
     *@Param file 字段名 object查询参数
     *@return Bean
     */
    public Integer findCountByFiled(String filed, Object o){
        String sql = MessageFormat.format("SELECT COUNT(*) FROM {0} where {1} = ''{2}''", entityClass.getSimpleName(), filed, o);
        Query queryCount = createHSQL(sql);
        Object num = queryCount.getSingleResult();
        return Integer.parseInt(num.toString());
    }

    /**
     *@author Jen
     *@Point 根据一个字段查询HSQL
     *@Param file 字段名 object查询参数
     *@return List
     */
    public List<T> findListByFiled(String filed, Object o){
        String sql = MessageFormat.format("FROM {0} where {1} = ''{2}''", entityClass.getSimpleName(), filed, o);
        List<T> list = createHSQLList(sql);
        entityManager.close();
        return list;
    }

    /**
     *@author Jen
     *@Point SQL查询
     *@return List
     */
    public List findListBySQL(String sql){
        List list = createSQLList(sql);
        entityManager.close();
        return list;
    }

    /**
     *@author Jen
     *@Point HSQL查询
     *@return List
     */
    public List<T> findListByHSQL(String hsql){
        List<T> list = createHSQLToList(hsql);
        entityManager.close();
        return list;
    }

    /**
     *@author Jen
     *@Point 分页查询
     *@Param Page
     *@return List
     */
    public Map findListByPage(Page page){
        final String[] sql = {MessageFormat.format("FROM {0} ", entityClass.getSimpleName())};
        JSONObject jsonObject = page.getData();
        HashMap<String,Object> hashMap = new HashMap<>();
        StringBuilder countSql = new StringBuilder();
        if(jsonObject!=null) {
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if(entry.getKey().equals("desc")||entry.getKey().equals("asc")){
                    hashMap.put(entry.getKey(),entry.getValue());
                }else if(sql[0].contains("where")){
                        sql[0] += " and " + entry.getKey() + "= '" + entry.getValue() + "'";
                        countSql.append(" and ").append(entry.getKey()).append("= '").append(entry.getValue()).append("'");
                }else {
                        sql[0] += " where " + entry.getKey() + "= '" + entry.getValue() + "'";
                        countSql.append(" where ").append(entry.getKey()).append("= '").append(entry.getValue()).append("'");
                    }
                }
            hashMap.forEach((k,v) -> sql[0] += " order by "+v+" "+k);
        }
        Query queryCount = createHSQL("SELECT COUNT(*) FROM "+entityClass.getSimpleName()+countSql);
        Object num = queryCount.getSingleResult();
        Query query = createHSQL(sql[0]);
        if(page.getLimit()!=null&page.getPage()!=null) {
            query.setFirstResult((page.getPage() - 1) * page.getLimit());
            query.setMaxResults(page.getLimit());
        }
        List<T> list = getHSQLList(query);
        entityManager.close();
        Map<String,Object> map = new HashMap<>();
        map.put("count",num);
        map.put("data",list);
        return map;
    }


    /**
     *@author Jen
     *@Point 不等于该字段查询
     *@Param file 字段名 object查询参数
     *@return List
     */
    public List<T> findListNotByFiled(String filed, Object o){
        String sql = MessageFormat.format("FROM {0} where {1} != ''{2}''", entityClass.getSimpleName(), filed, o);
        List<T> list = createHSQLList(sql);
        entityManager.close();
        return list;
    }

    /**
     *@author Jen
     *@Point 根据一个字段查询一个字段List
     *@Param file 字段名 object查询参数
     *@return List
     */
    public List<String> findListByFiled(String tField,String field, Object o){
        String sql = MessageFormat.format("select {0} FROM {1} where {2} = ''{3}''", tField,entityClass.getSimpleName(), field, o);
        List<String> list = createHSQLToList(sql);
        entityManager.close();
        return list;
    }

    /**
     *@author Jen
     *@Point 查询一个字段不重复List
     *@Param file 字段名 object查询参数
     *@return List
     */
    public List<String> findListFiledForDistinct(String tFiled){
        String sql = MessageFormat.format("select distinct {0} FROM {1}", tFiled,entityClass.getSimpleName());
        List<String> list = (List<String>)createHSQLList(sql);
        entityManager.close();
        return list;
    }
    /**
     *@author Jen
     *@Point 根据一个字段多个值查询
     *@Param file 字段名 object查询参数
     *@return List
     */
    public List<T> findListByFiledIn(String filed, List<String> list,HashMap<String,Object> hashMap){
        final String[] sql = {"from "+entityClass.getSimpleName()+" where "+ filed +" in (:list)"};
        sql[0] = getSql(sql[0],hashMap);
        Query query = createHSQL(sql[0]);
        query.setParameter("list",list);
        entityManager.close();
        return (List<T>)query.getResultList();
    }
    /**
     *@author Jen
     *@Point 查询一个Int字段最大值
     *@Param file 字段名 object查询参数
     *@return List
     */
    public Integer findMaxForFiled(String tFiled){
        String sql = MessageFormat.format("select max({0}) FROM {1} ", tFiled,entityClass.getSimpleName());
        String num = createHSQLMax(sql).toString();
        entityManager.close();
        return new Integer(num);
    }

    public Integer findMaxByFiled(String tFiled,String filed,String value){
        String sql = MessageFormat.format("select max({0}) FROM {1} where {2} = ''{3}'' ", tFiled,entityClass.getSimpleName(),filed,value);
        String num = createHSQLMax(sql).toString();
        entityManager.close();
        return new Integer(num);
    }

    /**
     *@Point HSQl 创建查询 查询为空 请抛出NoResultException
     *@Param HSQl
     *@Exception NoResultException
     *@return Query
     */
    private Query createHSQL(String SQL) {
        return this.getEntityManager().createQuery(SQL);
    }

    /**
     *@Point 查询结果为Map
     *@Param SQl
     *@Exception NoResultException
     *@return HashMap
     */
    public List getListMapBySQLQuery(String sql) {
        try {
            return createSQLMap(sql).getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *@Point 查询结果为Map
     *@Param SQl
     *@Exception NoResultException
     *@return HashMap
     */
    public HashMap getMapBySQLQuery(String sql) {
        try {
            return (HashMap) createSQLMap(sql).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *@Point SQl 创建查询为Map
     *@Param SQl
     *@return Query
     */
    private Query createSQLMap(String SQL) {
        Query query = this.getEntityManager().createNativeQuery(SQL);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query;
    }

    /**
     *@Point SQl 创建查询 查询为空 请抛出NoResultException
     *@Param SQl
     *@Exception NoResultException
     *@return Query
     */
    private <T> T createSQLSingle(String SQL){
        try {
            return (T)this.getEntityManager().createNativeQuery(SQL).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *@Point HSQl 创建查询 查询为空 请抛出NoResultException
     *@Param HSQl
     *@Exception NoResultException
     *@return Query
     */
    private <T> T createHSQLSingle(String SQL) {
        try {
            return (T)this.getEntityManager().createQuery(SQL).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     *@Point HSQl 创建查询 查询为空 请抛出NoResultException
     *@Param HSQl
     *@return Query
     */
    private List<T> createHSQLList(String SQL) {
        try {
            return this.getEntityManager().createQuery(SQL).getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }
    private List<T> getHSQLList(Query query) {
        try {
            return query.getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }
    private List createHSQLToList(String SQL) {
        try {
            return this.getEntityManager().createQuery(SQL).getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }
    private List createSQLList(String SQL) {
        try {
            return this.getEntityManager().createNativeQuery(SQL).getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }

    /**
     *@Point HSQl 创建查询 查询为空 请抛出NoResultException
     *@Param HSQl
     *@return Query
     */
    private Object createHSQLMax(String SQL) {
        try {
            Object object =  this.getEntityManager().createQuery(SQL).getSingleResult();
           if(object==null)return -1;
           return object;
        }catch (NoResultException e){
            return -1;
        }
    }
    /**
     *@author Jen
     *@Point 得到sql查询条件
     *@Param sql hashMap
     *@return String
     */
    private String getSql(String sql,HashMap<String,Object> hashMap){
        final String[] SQL = {sql};
        hashMap.forEach((k,v) -> {
            if(k.equals("desc")||k.equals("asc")){
                SQL[0] += " order by "+v+" "+k;
            }else if(sql.contains("where")){
                SQL[0] += " and "+k+" = '"+v+"'";
            }else if(!sql.contains("where")){
                SQL[0] += "where "+k+" = '"+v+"'";
            }

        });
        return SQL[0];
    }
    /**
     *@author Jen
     *@Point 得到updateSql
     *@Param sql hashMap
     *@return String
     */
    private String getSetSql(HashMap<String,Object> hashMap){
        final String[] SQL = {"set "};
        hashMap.forEach((k,v) -> SQL[0] += k+" = '"+v+"',");
        return SQL[0].substring(0, SQL[0].length() - 1);
    }
    /**
     *@author Jen
     *@Point 得到实体类Column注解name
     *@Param clazz
     *@return HashMap
     */
    private static HashMap<String, Object> getEntityFieldsMap(Class<?> clazz) {
        HashMap<String, Object> entityFieldsMap = new HashMap<>();
        Field[] videoFields = clazz.getDeclaredFields();
        Column presentColumn;
        String columnName;
        for (Field field : videoFields) {
            if (field.isAnnotationPresent(Column.class)) {
                presentColumn = field.getDeclaredAnnotation(Column.class);
                columnName = presentColumn.name();
                entityFieldsMap.put(columnName, field.getName());
        }
    }
    return entityFieldsMap;
}

    /**
     *@author Jen
     *@Point 得到实体类主键注解
     *@Param clazz
     *@return HashMap
     */
    private static String getEntityPrimary(Class<?> clazz) {
        Field[] videoFields = clazz.getDeclaredFields();
        Column presentColumn;
        for (Field field : videoFields) {
            if (field.isAnnotationPresent(Id.class)){
                if(field.isAnnotationPresent(Column.class)){
                    presentColumn = field.getDeclaredAnnotation(Column.class);
                    return presentColumn.name();
                }
                return field.getName();
            }
        }
        return null;
    }
    /**
     *@author Jen
     *@Point 将map集合中的数据转化为指定对象的同名属性中
     *@Param map clazz
     *@return T 实体类
     */
    public  <T> T MapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        if(map==null)return null;
        HashMap<String, Object> entityMap = new HashMap<>();
        HashMap<String, Object> oldMap = new HashMap<>();
        HashMap<String, Object> fileMap = getEntityFieldsMap(clazz);
        fileMap.forEach((k,v) -> map.forEach((k1, v1) -> {
            if(k.equals(k1)){
                entityMap.put(v.toString(),v1);
                oldMap.put(k1,v1);
            }
        }));
        oldMap.forEach((k,v) -> map.remove(k));
        map.putAll(entityMap);
        beanMap.putAll(map);
        return bean;
    }
}