package com.example.myproject.service.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.Page;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.entity.view.UserRoleView;
import com.example.myproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-19 10:47
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AllDao.UserRoleDao userRoleDao;
    @Autowired
    private AllDao.UserDao userDao;
    @Override
    public User findByFiled(String filed, Object object) {
        return userDao.findSingleBeanByFiled(filed,object);
    }
    @Override
    public List<UserRole> findListByFiled(String filed, Object object) {
        return userRoleDao.findListByFiled(filed,object);
    }

    @Override
    public Map findListByPage(Page page) {
        return userDao.findListByPage(page);
    }
}
