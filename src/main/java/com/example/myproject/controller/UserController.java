package com.example.myproject.controller;

import com.example.myproject.entity.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.User;
import com.example.myproject.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-25 12:10
 **/
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private IUserService userServiceImpl;

    @RequestMapping("/userList")
    public String roleList(){
        return "user/userList";
    }

    @ResponseBody
    @RequestMapping("/getUserPage")
    public SystemResponse getUserPage(Page page){
        try {
            Map map =  userServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @RequestMapping("/addOrEditUser")
    public ModelAndView addOrEditUser(String id){
        ModelAndView modelAndView = new ModelAndView("user/addOrEditUser");
        modelAndView.addObject("user",new User());
        if(StringUtils.isNotEmpty(id)){
            try {
                modelAndView.addObject("user",userServiceImpl.findById(id));
            }catch (Exception e){
                e.printStackTrace();
                modelAndView.addObject("user",new User());
            }
        }
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/addOrUpdateUser")
    public SystemResponse addOrUpdateUser(@RequestBody User user){
        try {
            return new SystemResponse().success().message(userServiceImpl.addOrUpdateUser(user));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @PostMapping("/checkUser")
    public SystemResponse checkUser(String account){
        try {
            return new SystemResponse().success().message(userServiceImpl.checkUser(account));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @PostMapping("/deleteUser")
    public SystemResponse deleteUser(String id,String roleId){
        try {
            return new SystemResponse().success().message(userServiceImpl.deleteUser(id,roleId));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @PostMapping("/updateStatusLock")
    public SystemResponse updateStatusLock(String id,String statusLock){
        try {
            return new SystemResponse().success().message(userServiceImpl.updateStatusLock(id,statusLock));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
