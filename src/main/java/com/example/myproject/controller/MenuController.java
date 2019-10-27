package com.example.myproject.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.Menu;
import com.example.myproject.entity.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-21 10:42
 **/
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuServiceImpl;

    @RequestMapping("/menuList")
    public String menuList(){
        return "menu/menuList";
    }

    @ResponseBody
    @PostMapping("/menuTableTree")
    public SystemResponse getMenu(Page page){
        try {
            Map map =  menuServiceImpl.findAllMenuByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            return new SystemResponse().pageDataFail();
        }
    }
    @ResponseBody
    @GetMapping("/getMenuType")
    public SystemResponse getMenuType(){
        try {
            String[] menuArray = new String[]{"","目录","菜单","按钮"};
            List<String> list = menuServiceImpl.getMenuType("menuType");
            JSONArray jsonArray = new JSONArray();
            for (String str:list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",menuArray[new Integer(str)]);
                jsonObject.put("value",str);
                jsonArray.add(jsonObject);
            }
            return new SystemResponse().success().data(jsonArray);
        }catch (Exception e){
            return new SystemResponse().fail();
        }
    }
    @ResponseBody
    @GetMapping("/getPMenu")
    public SystemResponse getPMenu(){
        try {
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
            return new SystemResponse().success().data(jsonArray);
        }catch (Exception e){
            return new SystemResponse().fail();
        }
    }
    @RequestMapping("/addMenu")
    public String addMenu(){
        return "menu/addMenu";
    }

    @ResponseBody
    @GetMapping("/getOneMenuType")
    public SystemResponse getOneMenuType(String id){
        try {
            Menu menu = menuServiceImpl.getOneMenuType("menuId",id);
            return new SystemResponse().success().data(menu.getMenuType());
        }catch (Exception e){
            return new SystemResponse().fail();
        }
    }

    @ResponseBody
    @PostMapping("/saveMenu")
    public SystemResponse saveMenu(@RequestBody Menu menu){
        try {
            menuServiceImpl.saveMenu(menu);
            return new SystemResponse().success().data(menu.getMenuType());
        }catch (Exception e){
            return new SystemResponse().fail();
        }
    }

    @ResponseBody
    @PostMapping("/delMenu")
    public SystemResponse delMenu(String id){
        try {
            menuServiceImpl.delMenu(id);
            return new SystemResponse().success().message("删除成功");
        }catch (Exception e){
            return new SystemResponse().fail().message("删除失败");
        }
    }

    @ResponseBody
    @PostMapping("/updateMenuByFiled")
    public SystemResponse updateMenuByFiled(String tField,String tValue,String field,String value ){
            try {
            Integer num = menuServiceImpl.updateMenuByFiled(tField,tValue,field,value);
            if(num>=0){
                return new SystemResponse().success().message("修改成功");
            }else  return new SystemResponse().fail().message("修改失败");
        }catch (Exception e){
            return new SystemResponse().fail().message("修改失败");
        }
    }

    @ResponseBody
    @PostMapping("/updateMenuById")
    public SystemResponse updateMenuById(String id,String field,String value ){
        try {
            Integer num = menuServiceImpl.updateMenuById(id,field,value);
            if(num>=0){
                return new SystemResponse().success().message("修改成功");
            }else  return new SystemResponse().fail().message("修改失败");
        }catch (Exception e){
            return new SystemResponse().fail().message("修改失败");
        }
    }

    @ResponseBody
    @PostMapping("/getCurrentMenuType")
    public SystemResponse getCurrentMenuType(String menuName){
        try {
            String[] menuArray = new String[]{"","目录","菜单","按钮"};
            JSONArray jsonArray = new JSONArray();
            Menu menu = menuServiceImpl.getOneMenuType("menuName",menuName);
            List<Menu> list = menuServiceImpl.getCurrentMenuType(menu);
            SystemResponse systemResponse =new SystemResponse();
            Optional.ofNullable(list).ifPresent(
                l->{
                    Integer num = new Integer(l.get(0).getMenuType());
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
            return new SystemResponse().fail();
        }
    }
    @ResponseBody
    @RequestMapping("/getMenuPage")
    public SystemResponse getJobPage(Page page){
        try {
            Map map =  menuServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            return new SystemResponse().pageDataFail();
        }
    }
}
