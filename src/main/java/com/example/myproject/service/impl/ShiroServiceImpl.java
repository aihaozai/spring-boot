package com.example.myproject.service.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.MenuPermission;
import com.example.myproject.entity.Page;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.service.IShiroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 13:14
 **/
@Service
public class ShiroServiceImpl implements IShiroService {
    @Autowired
    private AllDao.UserDao userDao;
    @Autowired
    private AllDao.UserRoleDao userRoleDao;
    @Autowired
    private AllDao.MenuPermissionDao menuPermissionDao;

    @Override
    public User findByFiled(String filed, Object object) {
        return userDao.findSingleBeanByFiled(filed, object);
    }

    @Override
    public List<UserRole> findListByFiled(String filed, Object object) {
        return userRoleDao.findListByFiled(filed, object);
    }

    @Override
    public List<MenuPermission> findListByFiledIn(String filed, List<Object> list) {
        return menuPermissionDao.findListByFiledIn(filed, list,new HashMap());
    }

}
