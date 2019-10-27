package com.example.myproject.service;

import com.example.myproject.entity.MenuPermission;

import java.util.List;

public interface IMenuPermissionService {
    List<MenuPermission> findListByFiledIn (String filed, List<Object> list);
}
