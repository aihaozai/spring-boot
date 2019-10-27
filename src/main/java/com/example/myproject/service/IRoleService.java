package com.example.myproject.service;

import com.example.myproject.entity.Page;
import com.example.myproject.entity.Person;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-19 11:16
 **/
public interface IRoleService {
    Map findListByPage(Page page);

    List<String> getRoleMenu(String tField, String field, Object object);

    List<String> getRoleUser(String tField, String field, Object object);

    String addOrUpdateRole(Person person);

    String authorizeMenu(String id, String roleName, String array);

    String authorizeUser(String personRole, String userRole);
}
