package com.example.myproject.service;

import com.example.myproject.entity.MenuPermission;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;

import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 14:30
 **/
public interface IShiroService {
    User findByFiled (String filed,Object object);

    List<UserRole> findListByFiled (String filed, Object object);

    List<MenuPermission> findListByFiledIn (String filed, List<Object> list);
}
