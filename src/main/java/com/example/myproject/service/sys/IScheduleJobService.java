package com.example.myproject.service.sys;

import com.example.myproject.common.Scheduled.ScheduleJob;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import org.quartz.SchedulerException;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-15 12:33
 **/
public interface IScheduleJobService {
    void saveScheduleJob(ScheduleJob scheduleJob);

    Map findListByPage(Page page);

    SystemResponse updateJobStatus(ScheduleJob scheduleJob)throws Exception;

    SystemResponse deleteJob(ScheduleJob scheduleJob)throws SchedulerException;

    SystemResponse updateJob(ScheduleJob scheduleJob)throws Exception;

    ScheduleJob findById(String id);

    void updateScheduleJob(ScheduleJob scheduleJob);
}
