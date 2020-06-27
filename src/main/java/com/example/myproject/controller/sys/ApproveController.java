package com.example.myproject.controller.sys;

import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.service.sys.IApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-06 11:37
 **/
@Api(tags = "审批接口")
@Controller
@RequestMapping("approve")
public class ApproveController {
    @Autowired
    private IApproveService approveServiceImpl;

    /**
     *@author Jen
     *@Description  获取要进行审批的用户角色
     */
    @ApiOperation("进行审批的用户角色页面接口")
    @GetMapping("/getApproveTaskLeader")
    public ModelAndView getApproveTaskLeader(String level){
        ModelAndView modelAndView = new ModelAndView("sys/approve/taskLeaderList");
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        modelAndView.addObject("leaders",approveServiceImpl.findLeader(user.getOrganId(),level));
        return modelAndView;
    }
}
