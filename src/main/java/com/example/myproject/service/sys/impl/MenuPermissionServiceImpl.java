package com.example.myproject.service.sys.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.sys.MenuPermission;
import com.example.myproject.service.sys.IMenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-19 15:22
 **/
@Service
public class MenuPermissionServiceImpl implements IMenuPermissionService {
    @Autowired
    private AllDao.MenuPermissionDao menuPermissionDao;
    @Override
    public List<MenuPermission> findListByFiledIn(String filed, List<String> list) {
        return menuPermissionDao.findListByFiledIn(filed, list,new HashMap());
    }
}
