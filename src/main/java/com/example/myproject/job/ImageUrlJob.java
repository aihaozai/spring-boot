package com.example.myproject.job;

import com.example.myproject.common.utils.imageUtil.MeiZiTuUtil;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-14 13:32
 **/
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class ImageUrlJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data=context.getTrigger().getJobDataMap();
        String invokeParam =(String) data.get("invokeParam");
        try {
            MeiZiTuUtil.getClassification("https://www.mzitu.com/xinggan","newMeizi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
