package com.example.myproject.service.sys.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.sys.MenuRole;
import com.example.myproject.entity.sys.Person;
import com.example.myproject.entity.sys.UserRole;
import com.example.myproject.entity.sys.view.PersonMenuView;
import com.example.myproject.service.sys.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-19 11:17
 **/
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private AllDao.RoleDao roleDao;
    @Autowired
    private AllDao.UserRoleDao userRoleDao;
    @Autowired
    private AllDao.MenuRoleDao menuRoleDao;
    @Autowired
    private AllDao.PersonMenuViewDao personMenuViewDao;
    @Autowired
    private AllDao.PermissionRoleDao permissionRoleDao;
    @Override
    public Map findListByPage(Page page) {
        return roleDao.findListByPage(page);
    }

    @Override
    public List<String> getRoleMenu(String tField, String field, Object object) {
        return personMenuViewDao.findListByFiled(tField,field,object);
    }

    @Override
    public List<String> getRoleUser(String tField, String field, Object object) {
        return userRoleDao.findListByFiled(tField,field,object);
    }

    @Override
    public String addOrUpdateRole(Person person) {
        if(StringUtils.isNotEmpty(person.getId())){
            roleDao.update(person);
            return "修改成功";
        }else {
            person.setId(UUIDUtil.randomUUID());
            roleDao.save(person);
            return "添加成功";
        }
    }
    @Override
    @Transactional
    public String authorizeMenu(String id, String roleName, String array) {
        List<String> newList = new ArrayList<>(Arrays.asList(array.split(",")));
        List<String> oldList = menuRoleDao.findListByFiled("roleMenu","roleId",id);
        List<String> existList = new ArrayList<>();
        for(String oldId:oldList){
            for(String newId:newList){
                if(oldId.equals(newId)){
                    existList.add(newId);
                }
            }
        }
        oldList.removeAll(existList);   //删除的
        newList.removeAll(existList);   //添加的
        for (String roleMenu:newList){
            MenuRole personRole = new MenuRole();
            personRole.setRoleId(id);
            personRole.setRoleName(roleName);
            personRole.setId(UUIDUtil.randomUUID());
            personRole.setRoleMenu(roleMenu);
            menuRoleDao.save(personRole);
        }
        for (String roleMenu:oldList){
            menuRoleDao.deleteByFiled("roleId",id,"roleMenu",roleMenu);
        }
        return "菜单授权成功";
    }

    @Override
    @Transactional
    public String authorizeUser(String personRole, String userRoleArray) {
        List<String> newList = new ArrayList<>(Arrays.asList(userRoleArray.split(",")));
        List<String> oldList = userRoleDao.findListByFiled("userRole","personRole",personRole);
        List<String> existList = new ArrayList<>();
        for(String oldId:oldList){
            for(String newId:newList){
                if(oldId.equals(newId)){
                    existList.add(newId);
                }
            }
        }
        oldList.removeAll(existList);   //删除的
        newList.removeAll(existList);   //添加的
        for (String userRole:newList){
            UserRole u = new UserRole();
            u.setId(UUIDUtil.randomUUID());
            u.setUserRole(userRole);
            u.setPersonRole(personRole);
            userRoleDao.save(u);
        }
        for (String userRole:oldList){
            userRoleDao.deleteByFiled("userRole",userRole,"personRole",personRole);
        }
        return "用户授权成功";
    }

    @Override
    @Transactional
    public void delRole(String id) {
        roleDao.deleteByFiled("id",id);
        menuRoleDao.deleteByFiled("roleId",id);
        userRoleDao.deleteByFiled("personRole",id);
    }

    @Override
    public List<PersonMenuView> findListByFiledIn(String roleId, List<String> collectionToList, HashMap hashMap) {
        return personMenuViewDao.findListByFiledIn(roleId,collectionToList,hashMap);
    }

    @Override
    public List<String> getPermissionRole(String menuId, String roleId) {
        return permissionRoleDao.findListByJSONObject(menuId,roleId);
    }

}
