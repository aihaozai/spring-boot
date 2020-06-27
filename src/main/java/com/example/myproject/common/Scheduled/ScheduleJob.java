package com.example.myproject.common.Scheduled;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="ScheduleJob表",description = "定时任务对象")
public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @ApiModelProperty(value = "主键",name = "quartzId",required = true)
    @Column(name = "quartz_id")
    private String quartzId;

    @ApiModelProperty(value = "执行类名",name = "beanName")
    @Column(name = "bean_name")
    private String beanName;

    @ApiModelProperty(value = "任务名称",name = "jobName")
    @Column(name = "job_name")
    private String jobName;

    @ApiModelProperty(value = "任务分组",name = "jobGroup")
    @Column(name = "job_group")
    private String jobGroup;

    @ApiModelProperty(value = "corn表达式",name = "cronExpression")
    @Column(name = "cron_expression")
    private String cronExpression;

    @ApiModelProperty(value = "需要传递的参数",name = "invokeParam")
    @Column(name = "invoke_param")
    private String invokeParam;

    @ApiModelProperty(value = "任务状态",name = "jobStatus")
    @Column(name = "job_status")
    private String jobStatus;

    @ApiModelProperty(value = "任务开始时间",name = "startTime")
    @Column(name = "start_time")
    private String startTime;

    @ApiModelProperty(value = "任务结束时间",name = "endTime")
    @Column(name = "end_time")
    private String endTime;

    @ApiModelProperty(value = "任务创建时间",name = "createTime")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "任务修改时间",name = "updateTime")
    @Column(name = "update_time")
    private String updateTime;
}
