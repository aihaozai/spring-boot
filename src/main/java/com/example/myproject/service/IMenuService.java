package com.example.myproject.service;

import com.example.myproject.entity.Menu;
import com.example.myproject.entity.Page;

import java.util.List;
import java.util.Map;

public interface IMenuService {
    Map findAllMenuByPage(Page page);

    List getMenuType(String menuType);

    List<Menu> getParentMenu(String field,Object value);

    Menu getOneMenuType(String menuId, String id);

    void saveMenu(Menu menu);

    void delMenu(String id);

    Integer updateMenuById(String id,String filed,String value) throws Exception;

    List getCurrentMenuType(Menu menu);

    Integer updateMenuByFiled(String tField, String tValue, String field, String value);

    Map findListByPage(Page page);
}
