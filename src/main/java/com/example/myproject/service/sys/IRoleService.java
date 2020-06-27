package com.example.myproject.service.sys;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.sys.Person;
import com.example.myproject.entity.sys.view.PersonMenuView;

import java.util.HashMap;
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

    void delRole(String id);

    List<PersonMenuView> findListByFiledIn(String roleId, List<String> collectionToList, HashMap hashMap);

    List<String> getPermissionRole(String menuId, String roleId);
}
