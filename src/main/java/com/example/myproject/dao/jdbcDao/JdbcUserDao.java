//package com.example.myproject.dao.jdbcDao;
//
//import com.example.myproject.entity.NewUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//
//@Repository
//public class JdbcUserDao {
//    @Autowired
//    @Qualifier("jdbcTemplateOne")
//    JdbcTemplate jdbcTemplateOne;
//    @Autowired
//    @Qualifier("jdbcTemplateTwo")
//    JdbcTemplate jdbcTemplateTwo;
//
//    public List<NewUser> getAllUserOne(){
//        String sql = "select * from user";
//        return jdbcTemplateOne.query(sql,new BeanPropertyRowMapper<>(NewUser.class));
//    }
//    public List<NewUser> getAllUserTwo(){
//        String sql = "select * from user";
//        return jdbcTemplateTwo.query(sql,new BeanPropertyRowMapper<>(NewUser.class));
//    }
//}
