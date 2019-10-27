package com.example.myproject.service;

import com.example.myproject.entity.Page;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import java.util.List;
import java.util.Map;

public interface IUserService {
    User findByFiled (String filed,Object object);
    List<UserRole> findListByFiled (String filed, Object object);

    Map findListByPage(Page page);
}
