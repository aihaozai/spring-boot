package com.example.myproject.common.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-12 16:05
 **/

@Slf4j
@Service
public class JobService {
    @Autowired
    @Qualifier("scheduler")
    private Scheduler scheduler;
    /**
     * 新建一个任务
     */
    public String addJob(ScheduleJob scheduleJob) throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
            return "Illegal cron expression 执行时间格式不正确";   //表达式格式不正确
        }

        Class<?> clazz=Class.forName(scheduleJob.getBeanName());
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) clazz).withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();

        //表达式调度构建器(即任务执行的时间,不立即执行)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();

        //按新的cronExpression表达式构建一个新的trigger
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                .withSchedule(scheduleBuilder);
        if(StringUtils.isNotEmpty(scheduleJob.getStartTime())){
            triggerBuilder.startAt(df.parse(scheduleJob.getStartTime()));
        }
        if(StringUtils.isNotEmpty(scheduleJob.getStartTime())){
            triggerBuilder.endAt(df.parse(scheduleJob.getEndTime()));
        }
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();
        //传递参数
        if(scheduleJob.getInvokeParam()!=null && !"".equals(scheduleJob.getInvokeParam())) {
            trigger.getJobDataMap().put("invokeParam",scheduleJob.getInvokeParam());
        }
        Date ft = scheduler.scheduleJob(jobDetail, trigger);
        log.info(jobDetail.getKey() + " 已经在 : " + df.format(ft) + " 时运行，Cron表达式：" + trigger.getCronExpression());
        if(scheduleJob.getJobStatus().equals("N")){
            pauseJob(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        }
        return "success";
    }
    /**
     * 获取Job状态
     */
    public String getJobState(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        return scheduler.getTriggerState(triggerKey).name();
    }

    //暂停所有任务
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    //暂停任务
    public String pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return "fail";
        }else {
            scheduler.pauseJob(jobKey);
            return "success";
        }

    }

    //恢复所有任务
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    // 恢复某个任务
    public String resumeJob(String jobName, String jobGroup) throws SchedulerException {

        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return "fail";
        }else {
            scheduler.resumeJob(jobKey);
            return "success";
        }
    }

    //删除某个任务
    public String  deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = new JobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null ) {
            return "jobDetail is null";
        }else if(!scheduler.checkExists(jobKey)) {
            return "jobKey is not exists";
        }else {
            scheduler.deleteJob(jobKey);
            return "success";
        }

    }

    //修改任务
    public String  modifyJob(ScheduleJob scheduleJob)  throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
            return "Illegal cron expression";
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        JobKey jobKey = new JobKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //表达式调度构建器,不立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();
            //按新的cronExpression表达式重新构建trigger
            if(StringUtils.isNotEmpty(scheduleJob.getStartTime())&&StringUtils.isNotEmpty(scheduleJob.getEndTime())){
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).startAt(df.parse(scheduleJob.getStartTime())).endAt(df.parse(scheduleJob.getEndTime())).withSchedule(scheduleBuilder).build();
            }else if(StringUtils.isNotEmpty(scheduleJob.getStartTime())){
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).startAt(df.parse(scheduleJob.getStartTime())).withSchedule(scheduleBuilder).build();
            }else if(StringUtils.isNotEmpty(scheduleJob.getEndTime())){
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).endAt(df.parse(scheduleJob.getEndTime())).withSchedule(scheduleBuilder).build();
            }else {
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            }
            //修改参数
            if(StringUtils.isNotEmpty(scheduleJob.getInvokeParam())) {
                if(trigger.getJobDataMap()!=null) {
                    if (!trigger.getJobDataMap().get("invokeParam").equals(scheduleJob.getInvokeParam())) {
                        trigger.getJobDataMap().put("invokeParam", scheduleJob.getInvokeParam());
                    }
                }else {
                    if(StringUtils.isNotEmpty(scheduleJob.getInvokeParam()))
                    trigger.getJobDataMap().put("invokeParam", scheduleJob.getInvokeParam());
                }
            }
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
            if(scheduleJob.getJobStatus().equals("N")){
                pauseJob(scheduleJob.getJobName(),scheduleJob.getJobGroup());
            }
            return "success";
        }else {
            return "job or trigger not exists";
        }

    }
}
