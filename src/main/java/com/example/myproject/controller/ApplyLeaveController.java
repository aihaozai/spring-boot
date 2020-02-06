package com.example.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.ApplyLeave;
import com.example.myproject.entity.Menu;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.view.UserLoginView;
import com.example.myproject.service.IApplyLeaveService;
import com.example.myproject.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
 * @Create: 2020-01-02 22:27
 **/
@Controller
@RequestMapping("applyLeave")
public class ApplyLeaveController {
    @Autowired
    private IApplyLeaveService applyLeaveServiceImpl;

    @Autowired
    private IDictService dictServiceImpl;

    @RequestMapping("/applyLeaveList")
    public String applyLeaveList(){
        return "applyLeave/applyLeaveList";
    }

    @PostMapping("/addLeave")
    public ModelAndView addLeave(@RequestBody JSONObject jsonObject){
        ModelAndView modelAndView = new ModelAndView("applyLeave/addLeave");
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        modelAndView.addObject("user",user);
        String id = jsonObject.getString("id");
        if(StringUtils.isEmpty(id)){
            modelAndView.addObject("applyLeave",new ApplyLeave());
        }else {
            modelAndView.addObject("applyLeave",applyLeaveServiceImpl.findById(id));
        }
        modelAndView.addObject("type",jsonObject.getString("type"));
        modelAndView.addObject("leaveDict",dictServiceImpl.findDictByPName("请假类型"));
        modelAndView.addObject("approveDict",dictServiceImpl.findDictByPName("审批状态"));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getApplyLeavePage")
    public SystemResponse getApplyLeavePage(Page page){
        try {
            UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
            page.getData().put("applyUser",user.getId());
            Map map =  applyLeaveServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"), map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ResponseBody
    @PostMapping("/addOrUpdateApplyLeave")
    public SystemResponse addOrUpdateApplyLeave(ApplyLeave applyLeave){
        try {
            return applyLeaveServiceImpl.addOrUpdateApplyLeave(applyLeave);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }

    @ResponseBody
    @PostMapping("/checkProcess")
    public SystemResponse checkProcess(String id,String type){
        try {
            return applyLeaveServiceImpl.checkProcess(id,type);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @PostMapping("/completeProcess")
    public SystemResponse completeProcess(String tableId,String type){
        try {
            applyLeaveServiceImpl.completeProcess(tableId,type);
            return new SystemResponse().success();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
