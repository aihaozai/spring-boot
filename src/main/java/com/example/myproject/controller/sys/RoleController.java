package com.example.myproject.controller.sys;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.sys.Person;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-19 11:09
 **/
@Api(tags = "权限接口")
@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private IRoleService roleServiceImpl;

    @ApiOperation("授权页面接口")
    @GetMapping("/roleList")
    public String roleList(){
        return "sys/role/roleList";
    }

    @ApiOperation("角色分页接口")
    @ResponseBody
    @GetMapping("/getRolePage")
    public SystemResponse getRolePage(Page page){
        try {
            Map map =  roleServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("角色拥有菜单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tField", value = "得到结果",required = true),
            @ApiImplicitParam(name = "field", value = "查询条件",required = true),
            @ApiImplicitParam(name = "object", value = "查询条件值",required = true)
    })
    @ResponseBody
    @PostMapping("/getRoleMenu")
    public SystemResponse getRoleMenu(String tField, String field,Object object){
        try {
            List list = roleServiceImpl.getRoleMenu(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("用户拥有角色接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tField", value = "得到结果",required = true),
            @ApiImplicitParam(name = "field", value = "查询条件",required = true),
            @ApiImplicitParam(name = "object", value = "查询条件值",required = true)
    })
    @ResponseBody
    @PostMapping("/getRoleUser")
    public SystemResponse getRoleUser(String tField, String field,Object object){
        try {
            List list = roleServiceImpl.getRoleUser(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("新增编辑角色接口")
    @ResponseBody
    @PostMapping("/addOrUpdateRole")
    public SystemResponse addOrUpdateRole(@RequestBody Person person){
        try {
            return new SystemResponse().success().message(roleServiceImpl.addOrUpdateRole(person));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("角色授权菜单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id",required = true),
            @ApiImplicitParam(name = "roleName", value = "角色名称",required = true),
            @ApiImplicitParam(name = "array", value = "菜单字符串逗号分隔",required = true)
    })
    @ResponseBody
    @PostMapping("/authorizeMenu")
    public SystemResponse authorizeMenu(String id,String roleName,String array){
        try {
            return new SystemResponse().success().message(roleServiceImpl.authorizeMenu(id,roleName,array));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ApiOperation("角色授权用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personRole", value = "人员角色id",required = true),
            @ApiImplicitParam(name = "array", value = "用户字符串逗号分隔",required = true)
    })
    @ResponseBody
    @PostMapping("/authorizeUser")
    public SystemResponse authorizeUser(String personRole,String userRoleArray){
        try {
            return new SystemResponse().success().message(roleServiceImpl.authorizeUser(personRole,userRoleArray));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ApiOperation("删除角色接口")
    @ApiImplicitParam(name = "id", value = "角色id",required = true)
    @ResponseBody
    @PostMapping("/delRole")
    public SystemResponse delRole(String id){
        try {
            roleServiceImpl.delRole(id);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().delMsgFail();
        }
    }

    @ApiOperation("角色授权用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "菜单id",required = true),
            @ApiImplicitParam(name = "roleId", value = "角色id",required = true)
    })
    @ResponseBody
    @PostMapping("/getPermissionRole")
    public SystemResponse getPermissionRole(String menuId, String roleId){
        try {
            List list = roleServiceImpl.getPermissionRole(menuId,roleId);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
