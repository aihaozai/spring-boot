package com.example.myproject.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.Menu;
import com.example.myproject.entity.MenuPermission;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.PermissionRole;
import com.example.myproject.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-21 10:45
 **/
@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private AllDao.MenuDao menuDao;
    @Autowired
    private AllDao.MenuPermissionDao menuPermissionDao;
    @Autowired
    private AllDao.PermissionRoleDao permissionRoleDao;
    @Override
    public Map findAllMenuByPage(Page page) {
        return menuDao.findListByPage(page);
    }

    @Override
    public List<String> getMenuType(String menuType) {
        return menuDao.findListFiledForDistinct(menuType);
    }

    @Override
    public List<Menu> getParentMenu(String field, Object value) {
        return menuDao.findListNotByFiled(field,value);
    }

    @Override
    public Menu getMenuTypeById(String id) {
        return menuDao.findByID(id);
    }

    @Override
    public void saveMenu(Menu menu) {
        menu.setId(UUIDUtil.randomUUID());
        menu.setMenuId(UUIDUtil.randomUUID());
        menu.setCreateTime(UUIDUtil.currentTime());
        Integer num = menuDao.findMaxForFiled("sort");
        menu.setSort(num+1);
        menuDao.save(menu);
    }

    @Override
    public void delMenu(String id) {
        List<String> list = menuDao.findListByFiled("menuId","pid",id);
        delChildren(list);
        menuDao.deleteByFiled("menuId",id);
    }

    @Override
    public Integer updateMenuById(String id,String filed,String value) throws Exception{
        return menuDao.updateById(id,filed,value);
    }

    @Override
    public List<Menu> getCurrentMenuType(Menu menu) {
        //查询该菜单是否含有子菜单
        List<Menu> list = menuDao.findListByFiled("pid",menu.getMenuId());
        if(list.size()>0)return null;
        else return menuDao.findListByFiled("menuId",menu.getPid());
    }

    @Override
    public Map findListByPage(Page page) {
        return menuDao.findListByPage(page);
    }

    @Override
    public Map findMenuPermission(Page page) {
        return menuPermissionDao.findListByPage(page);
    }

    @Transactional
    @Override
    public void saveMenuPermission(MenuPermission menuPermission) {
        menuPermission.setId(UUIDUtil.randomUUID());
        menuPermissionDao.save(menuPermission);
    }

    @Transactional
    @Override
    public Integer updateMenuPermissionById(String id, String field, String value) throws Exception {
        return menuPermissionDao.updateById(id,field,value);
    }

    @Transactional
    @Override
    public void delMenuPermission(String id) {
        menuPermissionDao.delete(id);
    }

    @Override
    public String authAllMenuPermission(JSONObject jsonObject) {
        List<MenuPermission> permissionList = menuPermissionDao.findListByFiled("menuId",jsonObject.getString("menuId"));
        if(jsonObject.getString("checked").equals("true")){
            for(MenuPermission permission:permissionList){
                PermissionRole permissionRole = new PermissionRole();
                permissionRole.setId(UUIDUtil.randomUUID());
                permissionRole.setRoleId(jsonObject.getString("roleId"));
                permissionRole.setRoleName(jsonObject.getString("roleName"));
                permissionRole.setMenuId(permission.getMenuId());
                permissionRole.setRolePermission(permission.getId());
                permissionRoleDao.save(permissionRole);
            }
            return "授权成功";
        }else {
            permissionRoleDao.deleteByFiled("menuId",jsonObject.getString("menuId"));
            return "取消授权成功";
        }
    }

    @Override
    public String authMenuPermission(JSONObject jsonObject) {
        if(jsonObject.getString("checked").equals("true")){
            PermissionRole permissionRole = JSON.toJavaObject(jsonObject,PermissionRole.class);
            permissionRole.setId(UUIDUtil.randomUUID());
            permissionRoleDao.save(permissionRole);
            return "授权成功";
        }else {
            permissionRoleDao.deleteByFiled("rolePermission",jsonObject.getString("rolePermission"));
            return "取消授权成功";
        }
    }

    @Override
    public Menu getMenuTypeByField(String menuId, String menuId1) {
        return menuDao.findSingleBeanByFiled(menuId,menuId1);
    }

    private void delChildren(List<String> list){
        for (String childrenId:list){
            List<String> childrenList = menuDao.findListByFiled("menuId","pid",childrenId);
            delChildren(childrenList);
            menuDao.deleteByFiled("menuId",childrenId);
        }
    }
}
