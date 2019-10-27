package com.example.myproject.service;


//import com.example.myproject.dao.jdbcDao.JdbcUserDao;
//import com.example.myproject.common.baseDao.JpaUserDaoOne;
//import com.example.myproject.dao.jpaDao.Secondary.JpaUserDaotTwo;
//import com.example.myproject.entity.JpaUser;
//import com.example.myproject.entity.NewUser;
//import com.example.myproject.mapper.UserMapper;
//import com.example.myproject.mapper1.UserMapper1;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
////@CacheConfig(cacheNames = "users")
//public class UserService {
//    @Autowired
//    private JdbcUserDao jdbcUserDao;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private UserMapper1 userMapper1;
//    @Autowired
//    private JpaUserDaoOne jpaUserDaoOne;
//    @Autowired
//    private JpaUserDaotTwo jpaUserDaoTwo;
//    public String getUserById(Integer id){
//        System.out.println("get..."+id);
//        return  "user";
//    }
//    public void deleteUserById(Integer id){
//        System.out.println("delete..."+id);
//
//    }
//
//    public List<NewUser> getAllUser () {
//        return jdbcUserDao.getAllUserOne();
//    }
//    public void adduser(){
//        JpaUser jpaUser = new JpaUser();
//        jpaUser.setUsername("jpa");
//        jpaUser.setPassword("jpa");
//        jpaUser.setPhone("jpa");
//        jpaUserDaoOne.save(jpaUser);
//    }
//    @Cacheable
//    public Page<JpaUser> getUserByPage(Pageable pageable){
//           return jpaUserDaoOne.findAll(pageable) ;
//    }
//
//    public void getAllUserMoreDataSource () {
//        List listOne = jdbcUserDao.getAllUserOne();
//        List listTwo = jdbcUserDao.getAllUserTwo();
//        System.out.println(listOne);
//        System.out.println(listTwo);
//    }
//
//    public void getAllUserMoreDataSourceMybatis () {
//        List listOne = userMapper.getAllUser();
//        List listTwo = userMapper1.getAllUser();
//        System.out.println(listOne);
//        System.out.println(listTwo);
//    }
//    @Cacheable(value = "c1")
//    public void getAllUserMoreDataSourceJpa () {
//        JpaUser listOne = jpaUserDaoOne.getOne(1);
//        JpaUser listTwo = jpaUserDaoTwo.getOne(13);
//
//        System.out.println(listOne);
//        System.out.println(listTwo);
//    }
//}
