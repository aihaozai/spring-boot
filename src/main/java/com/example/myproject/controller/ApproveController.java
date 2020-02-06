package com.example.myproject.controller;

import com.example.myproject.entity.view.UserLoginView;
import com.example.myproject.service.IApproveService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-06 11:37
 **/
@Controller
@RequestMapping("approve")
public class ApproveController {
    @Autowired
    private IApproveService approveServiceImpl;

    /**
     *@author Jen
     *@Description  获取要进行审批的用户角色
     */
    @RequestMapping("/getApproveTaskLeader")
    public ModelAndView getApproveTaskLeader(String level){
        ModelAndView modelAndView = new ModelAndView("approve/taskLeaderList");
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        modelAndView.addObject("leaders",approveServiceImpl.findLeader(user.getOrganId(),level));
        return modelAndView;
    }
}
