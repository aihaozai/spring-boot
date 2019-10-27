//package com.example.myproject.common.baseDao;
//
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.data.rest.core.annotation.RestResource;
//
//import java.util.List;
//
//@RepositoryRestResource (path = "us",collectionResourceRel = "us" ,itemResourceRel = "u")
//public interface JpaUserDaoOne extends JpaRepository<JpaUser,Integer> {
//    @RestResource(path ="username", rel ="username")
//    List<JpaUser> findByusernameContains(@Param("username") String username);
//
//}
