package com.example.myproject.controller.sys;


import com.example.myproject.common.Scheduled.JobService;
import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IScheduleJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-11 16:16
 **/
@Api(tags = "定时任务接口")
@RequestMapping("/job")
@Controller
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private IScheduleJobService scheduleJobServiceImpl;

    @ApiOperation("定时任务页面接口")
    @RequestMapping("/jobList")
    public String JobList(){
        return "sys/job/jobList";
    }

    @ApiOperation("定时任务分页接口")
    @ResponseBody
    @GetMapping("/getJobPage")
    public SystemResponse getJobPage(Page page){
        try {
            Map map =  scheduleJobServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("添加编辑定时任务页面接口")
    @GetMapping("/addOrEditJob")
    public ModelAndView addOrEditJob( String id){
        ModelAndView modelAndView = new ModelAndView("sys/job/addOrEditJob");
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

    @ApiOperation("保存定时任务接口")
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

    @ApiOperation("更新定时任务接口")
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

    @ApiOperation("更新定时任务状态接口")
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

    @ApiOperation("删除定时任务接口")
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

}
