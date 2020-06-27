package com.example.myproject.controller.sys;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.sys.User;
import com.example.myproject.service.sys.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "用户接口")
@RequestMapping("/user")
@Controller
public class UserController{
    @Autowired
    private IUserService userServiceImpl;

    @ApiOperation("用户页面接口")
    @RequestMapping("/userList")
    public String roleList(){
        return "sys/user/userList";
    }

    @ApiOperation("用户分页接口")
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
    @ApiOperation("用户添加编辑页面接口")
    @ApiImplicitParam(name = "id", value = "用户id",required = true)
    @PostMapping("/addOrEditUser")
    public ModelAndView addOrEditUser(String id){
        ModelAndView modelAndView = new ModelAndView("sys/user/addOrEditUser");
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

    @ApiOperation("用户添加编辑接口")
    @ResponseBody
    @PostMapping("/addOrUpdateUser")
    public SystemResponse addOrUpdateUser(@RequestBody User user){
        try {
            return new SystemResponse().success().message(userServiceImpl.addOrUpdateUser(user));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("检查用户账号存在接口")
    @ApiImplicitParam(name = "account", value = "用户账号",required = true)
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

    @ApiOperation("删除用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",required = true),
            @ApiImplicitParam(name = "roleId", value = "用户角色id",required = true)
    })
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

    @ApiOperation("更新用户锁定状态接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",required = true),
            @ApiImplicitParam(name = "statusLock", value = "锁定状态",required = true)
    })
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
