package com.example.myproject.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.HandlerInterceptor.WebSocketHandler;
import com.example.myproject.common.baseController.BaseController;
import com.example.myproject.common.utils.MenuTreeUtil;
import com.example.myproject.common.utils.TransformUtil;
import com.example.myproject.common.pojo.MenuTree;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.sys.view.PersonMenuView;
import com.example.myproject.common.shiro.ShiroHelper;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.service.sys.IActHiTaskProcessService;
import com.example.myproject.service.sys.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.myproject.common.utils.MenuTreeUtil.Pid;


/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-08-12 10:53
 **/
@Api(tags = "登录接口")
@Controller
public class LoginController extends BaseController {

    @Autowired
    private IRoleService roleServiceImpl;
    @Autowired
    private IActHiTaskProcessService hiTaskProcessServiceImpl;
    @Autowired
    private RedisSessionDAO redisSessionDAO;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private ShiroHelper shiroHelper;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("登录页面接口")
    @GetMapping("/login")
    public String Login(){
        return "login";
    }

    @ApiOperation("登录验证接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账户",required = true),
            @ApiImplicitParam(name = "password", value = "密码",required = true)
    })
    @ResponseBody
    @PostMapping("/checkLogin")
    public SystemResponse checkLogin(String account,String password,String rememberMe){
        SystemResponse response = new SystemResponse();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
            if ("on".equals(rememberMe)) {
                token.setRememberMe(true);
            } else {
                token.setRememberMe(false);
            }
            super.login(token);     //主体提交登录请求到SecurityManager
            return new SystemResponse().success();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            response.put("message",e.getMessage());
        } catch (AuthenticationException e) {
            response.put("message","认证失败！");
        }
        return response;
    }

    @ApiOperation("强制下线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId", value = "sessionId",required = true),
            @ApiImplicitParam(name = "message", value = "提示信息",required = true)
    })
    @ResponseBody
    @PostMapping("/kickOff")
    public SystemResponse kickOff(String sessionId,String message){
        try {
            Session session = redisSessionDAO.readSession(sessionId);
            SimplePrincipalCollection coll = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            UserLoginView userLoginView = (UserLoginView)coll.getPrimaryPrincipal();
            redisSessionDAO.delete(session);
            sendMessageToUser(userLoginView.getId(),new TextMessage(message));
            return new SystemResponse().success();
        } catch (Exception e) {
            return new SystemResponse().fail();
        }
    }

    @ApiOperation("主页面接口")
    @GetMapping("/index")
    public String index(Model model){
        try {
            AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
            UserLoginView userLoginView = (UserLoginView)SecurityUtils.getSubject().getPrincipal();
            model.addAttribute("roles",authorizationInfo.getRoles());
            model.addAttribute("status",userLoginView.getPassword());
            if(!redisTemplate.hasKey(userLoginView.getId())){
                redisTemplate.opsForValue().set(userLoginView.getId(),"yes",5000, TimeUnit.MILLISECONDS);
            }
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return "login";
        }

    }

    @ApiOperation("渲染主页面接口")
    @GetMapping("/layout")
    public String layout(){
        return "layout";
    }

    @ApiOperation("当前用户菜单接口")
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

    @ApiOperation("百度地图页面接口")
    @GetMapping("/baiduMap")
    public String baiduMap(){
        return "baiduMap";
    }


    @ApiOperation("当前用户信息接口")
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
