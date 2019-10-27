package com.example.myproject.common.Scheduled;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-11 15:50
 **/
@Getter
@Setter
@Entity
@Table(name = "schedule_job")
public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "quartz_id")
    private String quartzId;  // 主键
    @Column(name = "bean_name")
    private String beanName;  //执行类名
    @Column(name = "job_name")
    private String jobName;  //任务名称
    @Column(name = "job_group")
    private String jobGroup;  //任务分组
    @Column(name = "cron_expression")
    private String cronExpression;  //corn表达式
    @Column(name = "invoke_param")
    private String invokeParam;//需要传递的参数
    @Column(name = "job_status")
    private String jobStatus;//任务状态
    @Column(name = "start_time")
    private String startTime;  //任务开始时间
    @Column(name = "end_time")
    private String endTime;  //任务结束时间
    @Column(name = "create_time")
    private String createTime;  //任务创建时间
    @Column(name = "update_time")
    private String updateTime;  //任务修改时间
}
