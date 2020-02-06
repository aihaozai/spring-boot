package com.example.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.view.UserLoginView;
import com.example.myproject.service.IActHiTaskProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-12 15:04
 **/
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IActHiTaskProcessService hiTaskProcessServiceImpl;

    @RequestMapping("/messageMng")
    public String messageMng(){
        return "message/messageMng";
    }

    @ResponseBody
    @RequestMapping("/getTaskPage")
    public SystemResponse getUserPage(Page page){
        try {
            UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
            PageHelper.startPage(page.getPage(),page.getLimit(),page.getData().getString("orderBy"));
            page.getData().put("userId",user.getId());
            List<ActHiTaskProcess> list= hiTaskProcessServiceImpl.findAll(page.getData());
            PageInfo<ActHiTaskProcess> pageInfo = new PageInfo<>(list);
            return new SystemResponse().pageData(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @PostMapping("/taskMng")
    public ModelAndView addLeave(@RequestBody JSONObject jsonObject){
        ModelAndView modelAndView = new ModelAndView("message/task/taskMng");
        modelAndView.addObject("tableId",jsonObject.getString("id"));
        modelAndView.addObject("taskId",jsonObject.getString("taskId"));
        modelAndView.addObject("proId",jsonObject.getString("proId"));
        modelAndView.addObject("url",jsonObject.getString("url"));
        modelAndView.addObject("type",jsonObject.getString("type"));
        modelAndView.addObject("taskType",jsonObject.getString("taskType"));
        return modelAndView;
    }
    @ResponseBody
    @PostMapping("/getMessageNum")
    public SystemResponse getMessageNum(){
        try {
            UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("needTask",hiTaskProcessServiceImpl.getTaskCount(user.getId(),"N"));
            return new SystemResponse().success().data(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail();
        }
    }
}
