package com.example.myproject.controller;

//import com.example.myproject.common.Scheduled.JobUtil;
import com.example.myproject.common.Scheduled.JobService;
import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IScheduleJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-11 16:16
 **/
@RequestMapping("/job")
@Controller
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private IScheduleJobService scheduleJobServiceImpl;

    @RequestMapping("/jobList")
    public String JobList(){
        return "job/jobList";
    }
    //添加job
    @RequestMapping("/addOrEditJob")
    public ModelAndView addOrEditJob( String id){
        ModelAndView modelAndView = new ModelAndView("job/addOrEditJob");
        modelAndView.addObject("scheduleJob",new ScheduleJob());
        if(StringUtils.isNotEmpty(id)){
            try {
                modelAndView.addObject("scheduleJob",scheduleJobServiceImpl.findById(id));
            }catch (Exception e){
                e.printStackTrace();
                modelAndView.addObject("scheduleJob",new ScheduleJob());
            }
        }
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/getJobPage")
    public SystemResponse getJobPage(Page page){
        try {
            Map map =  scheduleJobServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }
    //保存一个job
    @ResponseBody
    @PostMapping("/saveJob")
    public SystemResponse saveJob(@RequestBody ScheduleJob scheduleJob){
        String result ="";
        try {
            if(StringUtils.isNotEmpty(scheduleJob.getQuartzId())){
                result = jobService.modifyJob(scheduleJob);
                if(result.equals("success")){
                    scheduleJob.setUpdateTime(UUIDUtil.currentTime());
                    scheduleJobServiceImpl.updateScheduleJob(scheduleJob);
                }
            }else {
                result = jobService.addJob(scheduleJob);
                if(result.equals("success")){
                    scheduleJob.setQuartzId(UUIDUtil.randomUUID());
                    scheduleJob.setCreateTime(UUIDUtil.currentTime());
                    scheduleJobServiceImpl.saveScheduleJob(scheduleJob);
                }
            }
            return new SystemResponse().code(result).message(result);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(result);
        }
    }
    //更新job状态
    @ResponseBody
    @PostMapping("/updateJobStatus")
    public SystemResponse updateJobStatus(@RequestBody ScheduleJob scheduleJob){
        try {
            return scheduleJobServiceImpl.updateJobStatus(scheduleJob);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().error();
        }
    }
    //删除job
    @ResponseBody
    @PostMapping("/deleteJob")
    public SystemResponse deleteJob(@RequestBody ScheduleJob scheduleJob){
        try {
            return scheduleJobServiceImpl.deleteJob(scheduleJob);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    //更新job状态
    @ResponseBody
    @PostMapping("/updateJob")
    public SystemResponse updateJob(@RequestBody ScheduleJob scheduleJob){
        try {
            return scheduleJobServiceImpl.updateJob(scheduleJob);
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
