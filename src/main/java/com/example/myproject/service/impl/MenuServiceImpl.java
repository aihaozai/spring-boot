package com.example.myproject.service.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.Menu;
import com.example.myproject.entity.Page;
import com.example.myproject.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private AllDao.PersonMenuViewDao personMenuViewDao;
    @Override
    public Map findAllMenuByPage(Page page) {
        return menuDao.findListByPage(page);
    }

    @Override
    public List getMenuType(String menuType) {
        return menuDao.findListFiledForOneDistinct(menuType);
    }

    @Override
    public List<Menu> getParentMenu(String field, Object value) {
        return menuDao.findListNotByFiled(field,value);
    }

    @Override
    public Menu getOneMenuType(String menuId, String id) {
        return menuDao.findSingleBeanByFiled(menuId,id);
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
        List<String> list = menuDao.findListByFiledForOne("menuId","pid",id);
        delChildren(list);
        menuDao.deleteByFiled("menuId",id);
    }

    @Override
    public Integer updateMenuById(String id,String filed,String value) throws Exception{
        return menuDao.updateById(id,filed,value);
    }

    @Override
    public List getCurrentMenuType(Menu menu) {
        //查询该菜单是否含有子菜单
        List<Menu> list = menuDao.findListByFiled("pid",menu.getMenuId());
        if(list.size()>0)return null;
        else return menuDao.findListByFiled("menuId",menu.getPid());
    }

    @Override
    public Integer updateMenuByFiled(String tField, String tValue, String field, String value) {
        return menuDao.updateByFiled(tField,tValue,field,value);
    }

    @Override
    public Map findListByPage(Page page) {
        return menuDao.findListByPage(page);
    }

    private void delChildren(List<String> list){
        for (String childrenId:list){
            List<String> childrenList = menuDao.findListByFiledForOne("menuId","pid",childrenId);
            delChildren(childrenList);
            menuDao.deleteByFiled("menuId",childrenId);
        }
    }
}
