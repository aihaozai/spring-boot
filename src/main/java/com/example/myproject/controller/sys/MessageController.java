package com.example.myproject.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.service.sys.IActHiTaskProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-12 15:04
 **/
@Api(tags = "消息接口")
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IActHiTaskProcessService hiTaskProcessServiceImpl;

    @ApiOperation("消息页面接口")
    @GetMapping("/messageMng")
    public String messageMng(){
        return "sys/message/messageMng";
    }

    @ApiOperation("任务分页接口")
    @ResponseBody
    @GetMapping("/getTaskPage")
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

    @ApiOperation("任务详情接口")
    @PostMapping("/taskMng")
    public ModelAndView addLeave(@RequestBody JSONObject jsonObject){
        ModelAndView modelAndView = new ModelAndView("sys/message/task/taskMng");
        return setModel(modelAndView,jsonObject);
    }

    @ApiOperation("待办任务数量接口")
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

    private ModelAndView setModel(ModelAndView modelAndView,JSONObject jsonObject){
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            modelAndView.addObject(entry.getKey(),entry.getValue());
        }
        return modelAndView;
    }
}
