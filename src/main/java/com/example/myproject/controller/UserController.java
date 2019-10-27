package com.example.myproject.controller;

import com.example.myproject.entity.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping("/getUserPage")
    public SystemResponse getUserPage(Page page){
        try {
            Map map =  userServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            return new SystemResponse().pageDataFail();
        }
    }
}
