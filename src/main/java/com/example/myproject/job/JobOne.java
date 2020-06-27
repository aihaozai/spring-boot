package com.example.myproject.job;

import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-11 16:08
 **/
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class JobOne implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data=context.getTrigger().getJobDataMap();
        String invokeParam =(String) data.get("invokeParam");
        System.out.println(invokeParam+"...job");
        //在这里实现业务逻辑
    }
}
