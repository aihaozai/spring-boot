package com.example.myproject.service;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 13:13
 **/
public interface IUserService {
    List<UserRole> findListByFiled (String filed, Object object);

    Map findListByPage(Page page);

    User findById(String id);

    String addOrUpdateUser(User user);

    String checkUser(String account);

    String deleteUser(String id, String roleId);

    String updateStatusLock(String id, String statusLock) throws Exception;
}
