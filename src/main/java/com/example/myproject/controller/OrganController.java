package com.example.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.utils.SelectTreeUtil;
import com.example.myproject.entity.Organ;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.service.IDictService;
import com.example.myproject.service.IOrganService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.example.myproject.common.utils.SelectTreeUtil.Pid;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-20 15:29
 **/
@Controller
@RequestMapping("/organ")
public class OrganController {
    @Autowired
    private IOrganService organServiceImpl;
    @Autowired
    private IDictService dictServiceImpl;

    @RequestMapping("/organList")
    public String organList(){
        return "organ/organList";
    }

    @ResponseBody
    @RequestMapping("/getOrganPage")
    public SystemResponse getOrganPage(Page page){
        try {
            Map map =  organServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ResponseBody
    @RequestMapping("/getOrganPageMapper")
    public SystemResponse getOrganPageMapper(Page page){
        try {
            PageHelper.orderBy(page.getData().getString("orderBy"));
            //PageHelper.startPage(page.getPage(),page.getLimit(),page.getData().getString("orderBy"));
            List list= organServiceImpl.findAll(page.getData());
            PageInfo pageInfo = new PageInfo<>(list);
            return new SystemResponse().pageData(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ResponseBody
    @RequestMapping("/getRoleOrgan")
    public SystemResponse getRoleOrgan(String tField, String field,Object object){
        try {
            List list = organServiceImpl.getRoleOrgan(tField,field,object);
            return new SystemResponse().success().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/addOrUpdateOrgan")
    public SystemResponse addOrUpdateOrgan(Organ organ){
        try {
            return new SystemResponse().success().message(organServiceImpl.addOrUpdateOrgan(organ));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

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

    @ResponseBody
    @RequestMapping("/getCurrentOrganLevel")
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

    @ResponseBody
    @PostMapping("/updateOrganById")
    public SystemResponse updateOrganById(String id,String field,String value ){
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
