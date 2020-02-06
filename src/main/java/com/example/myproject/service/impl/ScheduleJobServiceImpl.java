package com.example.myproject.service.impl;

import com.example.myproject.common.Scheduled.JobService;
import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IScheduleJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-15 12:33
 **/
@Service
public class ScheduleJobServiceImpl implements IScheduleJobService {
    @Autowired
    private AllDao.ScheduleJobDao scheduleJobDao;
    @Autowired
    private JobService jobService;
    @Override
    public void saveScheduleJob(ScheduleJob scheduleJob) {
        scheduleJobDao.save(scheduleJob);
    }

    @Override
    public Map findListByPage(Page page) {
        return scheduleJobDao.findListByPage(page);
    }

    @Override
    public SystemResponse updateJobStatus(ScheduleJob scheduleJob) throws Exception{
        String result = "";
        if(scheduleJob.getJobStatus().equals("Y")){
            result = jobService.resumeJob(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        }else {
            result = jobService.pauseJob(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        }
        String msg = scheduleJob.getJobStatus().equals("Y")?"任务已开启":"任务已关闭";
        if(result.equals("success")){
            scheduleJobDao.updateById(scheduleJob.getQuartzId(),"jobStatus",scheduleJob.getJobStatus());
            return new SystemResponse().success().message(msg);
        }
        return new SystemResponse().fail().message(msg.replace("已","")+"失败");
    }

    @Override
    public SystemResponse deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        String result = jobService.deleteJob(scheduleJob);
        if(result.equals("success")){
            scheduleJobDao.delete(scheduleJob.getQuartzId());
            return new SystemResponse().success().message(result);
        }
        return new SystemResponse().fail().message(result);
    }

    @Override
    public SystemResponse updateJob(ScheduleJob scheduleJob)throws Exception{
        String result = jobService.modifyJob(scheduleJob);
        if(result.equals("success")){
            scheduleJobDao.updateById(scheduleJob.getQuartzId(),"cronExpression",scheduleJob.getCronExpression());
            return new SystemResponse().success().message(result);
        }
        return new SystemResponse().fail().message(result);
    }

    @Override
    public ScheduleJob findById(String id) {
        return scheduleJobDao.findByID(id);
    }

    @Override
    public void updateScheduleJob(ScheduleJob scheduleJob) {
        scheduleJobDao.update(scheduleJob);
    }
}
