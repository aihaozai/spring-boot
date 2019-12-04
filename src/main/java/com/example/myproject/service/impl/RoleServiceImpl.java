package com.example.myproject.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.*;
import com.example.myproject.entity.view.PersonMenuView;
import com.example.myproject.service.IRoleService;
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
    private AllDao.PersonRoleDao personRoleDao;
    @Autowired
    private AllDao.PersonMenuViewDao personMenuViewDao;
    @Override
    public Map findListByPage(Page page) {
        return roleDao.findListByPage(page);
    }

    @Override
    public List<String> getRoleMenu(String tField, String field, Object object) {
        return personMenuViewDao.findListByFiledForOne(tField,field,object);
    }

    @Override
    public List<String> getRoleUser(String tField, String field, Object object) {
        return userRoleDao.findListByFiledForOne(tField,field,object);
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
        List<String> oldList = personRoleDao.findListByFiledForOne("roleMenu","roleId",id);
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
            PersonRole personRole = new PersonRole();
            personRole.setRoleId(id);
            personRole.setRoleName(roleName);
            personRole.setId(UUIDUtil.randomUUID());
            personRole.setRoleMenu(roleMenu);
            personRoleDao.save(personRole);
        }
        for (String roleMenu:oldList){
            personRoleDao.deleteByFiled("roleId",id,"roleMenu",roleMenu);
        }
        return "菜单授权成功";
    }

    @Override
    @Transactional
    public String authorizeUser(String personRole, String userRoleArray) {
        List<String> newList = new ArrayList<>(Arrays.asList(userRoleArray.split(",")));
        List<String> oldList = userRoleDao.findListByFiledForOne("userRole","personRole",personRole);
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
        personRoleDao.deleteByFiled("roleId",id);
        userRoleDao.deleteByFiled("personRole",id);
    }

    @Override
    public List<PersonMenuView> findListByFiledIn(String roleId, List<Object> collectionToList, HashMap hashMap) {
        return personMenuViewDao.findListByFiledIn(roleId,collectionToList,hashMap);
    }

}
