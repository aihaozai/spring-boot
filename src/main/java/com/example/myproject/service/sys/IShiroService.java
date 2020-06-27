package com.example.myproject.service.sys;

import com.example.myproject.entity.sys.UserRole;
import com.example.myproject.entity.sys.view.MenuPermissionRoleView;
import com.example.myproject.entity.sys.view.UserLoginView;

import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 14:30
 **/
public interface IShiroService {
    UserLoginView findByFiled (String filed, Object object);

    List<UserRole> findListByFiled (String filed, Object object);

    List<MenuPermissionRoleView> findListByFiledIn (String filed, List<String> list);
}
