package com.example.myproject.controller;

import com.example.myproject.entity.Page;
import com.example.myproject.entity.Person;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-19 11:09
 **/
@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private IRoleService roleServiceImpl;

    @RequestMapping("/roleList")
    public String roleList(){
        return "role/roleList";
    }
    @ResponseBody
    @RequestMapping("/getRolePage")
    public SystemResponse getJobPage(Page page){
        try {
            Map map =  roleServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            return new SystemResponse().pageDataFail();
        }
    }
    @ResponseBody
    @RequestMapping("/getRoleMenu")
    public SystemResponse getRoleMenu(String tField, String field,Object object){
        try {
            List list = roleServiceImpl.getRoleMenu(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping("/getRoleUser")
    public SystemResponse getRoleUser(String tField, String field,Object object){
        try {
            List list = roleServiceImpl.getRoleUser(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping("/addOrUpdateRole")
    public SystemResponse addOrUpdateRole(Person person){
        try {
            return new SystemResponse().success().message(roleServiceImpl.addOrUpdateRole(person));
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping("/authorizeMenu")
    public SystemResponse authorizeMenu(String id,String roleName,String array){
        try {
            return new SystemResponse().success().message(roleServiceImpl.authorizeMenu(id,roleName,array));
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping("/authorizeUser")
    public SystemResponse authorizeUser(String personRole,String userRoleArray){
        try {
            return new SystemResponse().success().message(roleServiceImpl.authorizeUser(personRole,userRoleArray));
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
