package com.example.myproject.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.sys.Menu;
import com.example.myproject.entity.sys.MenuPermission;
import com.example.myproject.common.pojo.Page;

import java.util.List;
import java.util.Map;

public interface IMenuService {
    Map findAllMenuByPage(Page page);

    List<String> getMenuType(String menuType);

    List<Menu> getParentMenu(String field,Object value);

    Menu getMenuTypeById(String id);

    void saveMenu(Menu menu);

    void delMenu(String id);

    Integer updateMenuById(String id,String filed,String value) throws Exception;

    List<Menu> getCurrentMenuType(Menu menu);

    Map findListByPage(Page page);

    Map findMenuPermission(Page page);

    void saveMenuPermission(MenuPermission menuPermission);

    Integer updateMenuPermissionById(String id, String field, String value) throws Exception;

    void delMenuPermission(String id);

    String authAllMenuPermission(JSONObject jsonObject);

    String authMenuPermission(JSONObject jsonObject);

    Menu getMenuTypeByField(String menuId, String menuId1);
}
