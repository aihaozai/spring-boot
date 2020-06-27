package com.example.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.business.ApplyLeave;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.service.business.IApplyLeaveService;
import com.example.myproject.service.sys.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 22:27
 **/
@Api(tags = "请假接口")
@Controller
@RequestMapping("applyLeave")
public class ApplyLeaveController {
    @Autowired
    private IApplyLeaveService applyLeaveServiceImpl;

    @Autowired
    private IDictService dictServiceImpl;

    @ApiOperation("请假页面接口")
    @GetMapping(value = "/applyLeaveList")
    public String applyLeaveList(){
        return "applyLeave/applyLeaveList";
    }

    @ApiOperation("添加请假接口")
    @PostMapping(value = "/addLeave")
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
    @ApiOperation("请假列表分页接口")
    @ResponseBody
    @GetMapping(value = "/getApplyLeavePage")
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
    @ApiOperation("保存或编辑请假接口")
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

    @ApiOperation("检查请是否已审批接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id",required = true),
            @ApiImplicitParam(name = "type", value = "审批状态",required = true)
    })
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

    @ApiOperation("完成请假审批接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id",required = true),
            @ApiImplicitParam(name = "type", value = "审批状态",required = true)
    })
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
