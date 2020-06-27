package com.example.myproject.service.sys;

import com.example.myproject.entity.sys.MenuPermission;

import java.util.List;

public interface IMenuPermissionService {
    List<MenuPermission> findListByFiledIn (String filed, List<String> list);
}
