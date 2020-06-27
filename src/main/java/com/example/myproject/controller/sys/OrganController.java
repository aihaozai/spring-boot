package com.example.myproject.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.utils.SelectTreeUtil;
import com.example.myproject.entity.sys.Organ;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IDictService;
import com.example.myproject.service.sys.IOrganService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.myproject.common.utils.SelectTreeUtil.Pid;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-20 15:29
 **/
@Api(tags = "机构接口")
@Controller
@RequestMapping("/organ")
public class OrganController {
    @Autowired
    private IOrganService organServiceImpl;
    @Autowired
    private IDictService dictServiceImpl;

    @ApiOperation("机构页面接口")
    @GetMapping("/organList")
    public String organList(){
        return "sys/organ/organList";
    }

    @ApiOperation("机构分页接口")
    @ResponseBody
    @GetMapping("/getOrganPage")
    public SystemResponse getOrganPage(Page page){
        try {
            Map map =  organServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("机构分页接口")
    @ResponseBody
    @PostMapping("/getOrganPageMapper")
    public SystemResponse getOrganPageMapper(Page page){
        try {
            PageHelper.orderBy(page.getData().getString("orderBy"));
            List list= organServiceImpl.findAll(page.getData());
            PageInfo pageInfo = new PageInfo<>(list);
            return new SystemResponse().pageData(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("角色拥有机构接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tField", value = "得到结果",required = true),
            @ApiImplicitParam(name = "field", value = "查询条件",required = true),
            @ApiImplicitParam(name = "object", value = "查询条件值",required = true)
    })
    @ResponseBody
    @PostMapping("/getRoleOrgan")
    public SystemResponse getRoleOrgan(String tField, String field,Object object){
        try {
            List list = organServiceImpl.getRoleOrgan(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("添加更新机构接口")
    @ResponseBody
    @PostMapping("/addOrUpdateOrgan")
    public SystemResponse addOrUpdateOrgan(@RequestBody Organ organ){
        try {
            return new SystemResponse().success().message(organServiceImpl.addOrUpdateOrgan(organ));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("删除机构接口")
    @ApiImplicitParam(name = "organId", value = "机构id",required = true)
    @ResponseBody
    @PostMapping("/delOrgan")
    public SystemResponse delOrgan(String organId){
        try {
            organServiceImpl.delOrgan(organId);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().delMsgFail();
        }
    }

    @ApiOperation("角色授权机构接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id",required = true),
            @ApiImplicitParam(name = "roleName", value = "角色名称",required = true),
            @ApiImplicitParam(name = "organIds", value = "机构字符串逗号分隔",required = true)
    })
    @ResponseBody
    @PostMapping("/authOrgan")
    public SystemResponse authOrgan(String roleId,String roleName,String organIds){
        try {
            return new SystemResponse().success().message(organServiceImpl.authOrgan(roleId,roleName,organIds));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ApiOperation("树形机构接口")
    @ResponseBody
    @GetMapping("/organTree")
    public SystemResponse organTree(){
        try {
            return new SystemResponse().success().data(SelectTreeUtil.buildTree(Pid,organServiceImpl.findAll()));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("获得当前机构等级接口")
    @ApiImplicitParam(name = "organId", value = "机构id",required = true)
    @ResponseBody
    @PostMapping("/getCurrentOrganLevel")
    public SystemResponse getCurrentOrganLevel(String id){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("level",dictServiceImpl.findDictByPName("机构级别"));
            jsonObject.put("currentLevel",organServiceImpl.findById(id).getOrganLevel());
            return new SystemResponse().success().data(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("更新机构接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id",required = true),
            @ApiImplicitParam(name = "field", value = "更新目标",required = true),
            @ApiImplicitParam(name = "value", value = "更新目标值",required = true)
    })
    @ResponseBody
    @PostMapping("/updateOrganById")
    public SystemResponse updateOrganById(String id,String field,String value){
        try {
            Integer num = organServiceImpl.updateOrganById(id,field,value);
            if(num>=0){
                return new SystemResponse().success().updateMsgSuccess();
            }else  return new SystemResponse().fail().updateMsgFail();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
