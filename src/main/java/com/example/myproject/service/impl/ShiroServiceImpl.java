package com.example.myproject.service.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.entity.view.MenuPermissionRoleView;
import com.example.myproject.entity.view.UserLoginView;
import com.example.myproject.service.IShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 13:14
 **/
@Service
public class ShiroServiceImpl implements IShiroService {
    @Autowired
    private AllDao.UserRoleDao userRoleDao;
    @Autowired
    private AllDao.UserLoginViewDao userLoginViewDao;
    @Autowired
    private AllDao.MenuPermissionRoleViewDao menuPermissionRoleViewDao;
    @Override
    public UserLoginView findByFiled(String filed, Object object) {
        return userLoginViewDao.findSingleBeanByFiled(filed, object);
    }

    @Override
    public List<UserRole> findListByFiled(String filed, Object object) {
        return userRoleDao.findListByFiled(filed, object);
    }

    @Override
    public List<MenuPermissionRoleView> findListByFiledIn(String filed, List<String> list) {
        return menuPermissionRoleViewDao.findListByFiledIn(filed, list,new HashMap());
    }

}
