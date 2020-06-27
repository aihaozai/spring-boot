package com.example.myproject.controller.sys;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.sys.Menu;
import com.example.myproject.entity.sys.MenuPermission;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-21 10:42
 **/
@Api(tags = "菜单接口")
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuServiceImpl;

    @ApiOperation("菜单页面接口")
    @GetMapping("/menuList")
    public String menuList(){
        return "sys/menu/menuList";
    }

    @ApiOperation("菜单分页接口")
    @ResponseBody
    @PostMapping("/menuTableTree")
    public SystemResponse getMenu(Page page){
        try {
            Map map =  menuServiceImpl.findAllMenuByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("添加菜单页面接口")
    @GetMapping("/addMenu")
    public ModelAndView addMenu(){
        ModelAndView modelAndView = new ModelAndView("sys/menu/addMenu");
        List<Menu> list = menuServiceImpl.getParentMenu("menuType",3);
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("name","根目录");
        json.put("value","0");
        jsonArray.add(json);
        for (Menu menu:list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",menu.getMenuName());
            jsonObject.put("value",menu.getMenuId());
            jsonArray.add(jsonObject);
        }
        modelAndView.addObject("menuArray",jsonArray);
        return modelAndView;
    }

    @ApiOperation("查询某菜单类型接口")
    @ApiImplicitParam(name = "menuId", value = "菜单id",required = true)
    @ResponseBody
    @GetMapping("/getMenuTypeByField")
    public SystemResponse getMenuTypeByField(String menuId){
        try {
            Menu menu = menuServiceImpl.getMenuTypeByField("menuId",menuId);
            return new SystemResponse().success().data(menu.getMenuType());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("保存菜单接口")
    @ResponseBody
    @PostMapping("/saveMenu")
    public SystemResponse saveMenu(@RequestBody Menu menu){
        try {
            menuServiceImpl.saveMenu(menu);
            return new SystemResponse().success().data(menu.getMenuType());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("删除某菜单接口")
    @ApiImplicitParam(name = "menuId", value = "菜单id",required = true)
    @ResponseBody
    @PostMapping("/delMenu")
    public SystemResponse delMenu(String id){
        try {
            menuServiceImpl.delMenu(id);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().delMsgFail();
        }
    }

    @ApiOperation("更新菜单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id",required = true),
            @ApiImplicitParam(name = "field", value = "更新目标",required = true),
            @ApiImplicitParam(name = "value", value = "更新目标值",required = true)
    })
    @ResponseBody
    @PostMapping("/updateMenuById")
    public SystemResponse updateMenuById(String id,String field,String value ){
        try {
            Integer num = menuServiceImpl.updateMenuById(id,field,value);
            if(num>=0){
                return new SystemResponse().success().updateMsgSuccess();
            }else  return new SystemResponse().fail().updateMsgFail();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("查询当前菜单类型接口")
    @ApiImplicitParam(name = "menuId", value = "菜单id",required = true)
    @ResponseBody
    @PostMapping("/getCurrentMenuType")
    public SystemResponse getCurrentMenuType(String id){
        try {
            String[] menuArray = new String[]{"","目录","菜单","按钮"};
            JSONArray jsonArray = new JSONArray();
            Menu menu = menuServiceImpl.getMenuTypeById(id);
            List<Menu> list = menuServiceImpl.getCurrentMenuType(menu);
            SystemResponse systemResponse =new SystemResponse();
            Optional.ofNullable(list).ifPresent(
                l->{
                    Integer num = Integer.valueOf(l.get(0).getMenuType());
                    if(menuArray[num].equals("目录")){
                        for(int i=num;i<num+2;i++){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name",menuArray[i+1]);
                            jsonObject.put("value",i+1);
                            jsonArray.add(jsonObject);
                        }
                    }else if(menuArray[num].equals("菜单")&&menu.getMenuType().equals("3")){
                        systemResponse.message("该按钮类型所属菜单不能更改");
                    }
                }
            );
            return systemResponse.success().data(jsonArray);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("菜单分页接口")
    @ResponseBody
    @PostMapping("/getMenuPage")
    public SystemResponse getMenuPage(Page page){
        try {
            Map map =  menuServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("菜单权限分页接口")
    @ResponseBody
    @GetMapping("/menuTablePermission")
    public SystemResponse menuTablePermission(Page page){
        try {
            Map map =  menuServiceImpl.findMenuPermission(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("添加菜单权限页面接口")
    @GetMapping("/addMenuPermission")
    public String addMenuPermission(){
        return "sys/menu/addMenuPermission";
    }

    @ApiOperation("保存菜单权限接口")
    @ResponseBody
    @PostMapping("/saveMenuPermission")
    public SystemResponse saveMenuPermission(@RequestBody MenuPermission menuPermission){
        try {
            menuServiceImpl.saveMenuPermission(menuPermission);
            return new SystemResponse().success().addMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("更新菜单权限接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id",required = true),
            @ApiImplicitParam(name = "field", value = "更新目标",required = true),
            @ApiImplicitParam(name = "value", value = "更新目标值",required = true)
    })
    @ResponseBody
    @PostMapping("/updateMenuPermissionById")
    public SystemResponse updateMenuPermissionById(String id,String field,String value){
        try {
            Integer num = menuServiceImpl.updateMenuPermissionById(id,field,value);
            if(num>=0){
                return new SystemResponse().success().updateMsgSuccess();
            }else  return new SystemResponse().fail().updateMsgFail();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("删除菜单权限接口")
    @ApiImplicitParam(name = "id", value = "主键id",required = true)
    @ResponseBody
    @PostMapping("/delMenuPermission")
    public SystemResponse delMenuPermission(String id){
        try {
            menuServiceImpl.delMenuPermission(id);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("授权所有菜单权限接口")
    @ResponseBody
    @PostMapping("/authAllMenuPermission")
    public SystemResponse authAllMenuPermission(@RequestBody JSONObject jsonObject){
        try {
            return new SystemResponse().success().message(menuServiceImpl.authAllMenuPermission(jsonObject));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("授权菜单权限接口")
    @ResponseBody
    @PostMapping("/authMenuPermission")
    public SystemResponse authMenuPermission(@RequestBody JSONObject jsonObject){
        try {
            return new SystemResponse().success().message(menuServiceImpl.authMenuPermission(jsonObject));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
