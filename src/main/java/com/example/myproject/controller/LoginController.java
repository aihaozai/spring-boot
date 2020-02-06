package com.example.myproject.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseController.BaseController;
import com.example.myproject.common.utils.MenuTreeUtil;
import com.example.myproject.common.utils.TransformUtil;
import com.example.myproject.common.pojo.MenuTree;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.view.PersonMenuView;
import com.example.myproject.common.shiro.ShiroHelper;
import com.example.myproject.entity.view.UserLoginView;
import com.example.myproject.service.IActHiTaskProcessService;
import com.example.myproject.service.IRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.myproject.common.utils.MenuTreeUtil.Pid;


/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-08-12 10:53
 **/
@Controller
public class LoginController extends BaseController {

    @Autowired
    private IRoleService roleServiceImpl;
    @Autowired
    private IActHiTaskProcessService hiTaskProcessServiceImpl;

    @Autowired
    private ShiroHelper shiroHelper;

    @RequestMapping("/login")
    public String Login(){
        return "login";
    }

    @ResponseBody
    @PostMapping("/checkLogin")
    public SystemResponse checkLogin(@RequestBody JSONObject jsonObjectRequest){
        String username = jsonObjectRequest.getString("username");
        String password = jsonObjectRequest.getString("password");
        SystemResponse response = new SystemResponse();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            super.login(token);     //主体提交登录请求到SecurityManager
            return new SystemResponse().success();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            response.put("message",e.getMessage());
        } catch (AuthenticationException e) {
            response.put("message","认证失败！");
        }
        return response;
    }

    @GetMapping("/layout")
    public String layout(){
        return "layout";
    }

    @ResponseBody
    @PostMapping("/getMenus")
    public SystemResponse getMenu(){
        try {
            JSONObject resultObject = new JSONObject();
            AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
            HashMap hashMap = new HashMap();
            hashMap.put("asc","sort");
            List<PersonMenuView> personMenuViews =roleServiceImpl.findListByFiledIn("roleId", TransformUtil.CollectionToList(authorizationInfo.getRoles()),hashMap);
            List<MenuTree> menuTreeList = MenuTreeUtil.buildMenuTree(Pid,MenuTreeUtil.distinctMenuTree(personMenuViews));
            return new SystemResponse().success().data(menuTreeList);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().data(new ArrayList<>());
        }
    }

    @GetMapping("/baiduMap")
    public String baiduMap(){
        return "baiduMap";
    }

    @RequestMapping("/index")
    public String index(Model model){
        try {
            AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
            model.addAttribute("roles",authorizationInfo.getRoles());
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return "login";
        }

    }

    @ResponseBody
    @RequestMapping("/menuTableTree")
    public JSONObject permissionTree(){
        String data ="[{\"permissionId\":1,\"permissionName\":\"系统管理\",\"permissionUrl\":null,\"permissionType\":null,\"icon\":null,\"pid\":0,\"seq\":0,\"resType\":\"0\"},{\"permissionId\":2,\"permissionName\":\"账户管理\",\"permissionUrl\":\"/link/sysUsers\",\"permissionType\":\"管理登录的账户\",\"icon\":null,\"pid\":1,\"seq\":1,\"resType\":\"1\"},{\"permissionId\":3,\"permissionName\":\"部门管理\",\"permissionUrl\":\"/link/deparAdministrate\",\"permissionType\":\"部门的管理\",\"icon\":null,\"pid\":2,\"seq\":2,\"resType\":\"1\"},{\"permissionId\":4,\"permissionName\":\"角色管理\",\"permissionUrl\":\"/link/sysRoleManage\",\"permissionType\":\"设定角色的权限\",\"icon\":null,\"pid\":1,\"seq\":3,\"resType\":\"1\"},{\"permissionId\":5,\"permissionName\":\"权限管理\",\"permissionUrl\":\"/link/sysPermission\",\"permissionType\":\"对权限进行编辑\",\"icon\":null,\"pid\":1,\"seq\":4,\"resType\":\"1\"},{\"permissionId\":6,\"permissionName\":\"系统日志\",\"permissionUrl\":\"/link/sysLog\",\"permissionType\":\"系统日志\",\"icon\":null,\"pid\":1,\"seq\":5,\"resType\":\"1\"}]";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg",true);
        jsonObject.put("code",0);
        JSONArray myJson = JSONArray.parseArray(data);
        jsonObject.put("data",myJson);
        jsonObject.put("count",6);
        return jsonObject;
    }

    @ResponseBody
    @PostMapping("/getInfo")
    public SystemResponse getInfo(){
        try {
            JSONObject resultObject = new JSONObject();
            UserLoginView userLoginView = (UserLoginView)SecurityUtils.getSubject().getPrincipal();
            resultObject.put("userName",userLoginView.getUsername());
            resultObject.put("dot",hiTaskProcessServiceImpl.getTaskCount(userLoginView.getId(),"N"));
            return new SystemResponse().success().data(resultObject);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }
}
