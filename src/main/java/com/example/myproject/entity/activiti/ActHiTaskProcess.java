package com.example.myproject.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "act_hi_task_process")
@ApiModel(value="ActHiTaskProcess表",description = "审批历史任务流程对象")
public class ActHiTaskProcess implements Serializable {

    private static final long serialVersionUID = -1637407873763545068L;

    @Id
    @ApiModelProperty(value = "主键",name = "id",required = true)
    private String id;

    @ApiModelProperty(value = "流程实例id",name = "processId")
    @Column(name = "process_id")
    private String processId;

    @ApiModelProperty(value = "流程key",name = "processKey")
    @Column(name = "process_key")
    private String processKey;

    @ApiModelProperty(value = "表id",name = "tableId")
    @Column(name = "table_id")
    private String tableId;

    @ApiModelProperty(value = "任务名称",name = "taskName")
    @Column(name = "task_name")
    private String taskName;

    @ApiModelProperty(value = "发起人",name = "launchId")
    @Column(name = "launch_id")
    private String launchId;

    @ApiModelProperty(value = "会签人",name = "assignee")
    private String assignee;

    @ApiModelProperty(value = "任务完成状态",name = "status",example = "Y")
    private String status;

    @ApiModelProperty(value = "审批状态",name = "agreeStatus",example = "0")
    @Column(name = "agree_status")
    private Integer agreeStatus;

    @ApiModelProperty(value = "审批意见",name = "opinion")
    private String opinion;

    @ApiModelProperty(value = "审批开始时间",name = "startTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "start_time")
    private Timestamp startTime;

    @ApiModelProperty(value = "审批结束时间",name = "endTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "end_time")
    private Timestamp endTime;

    @ApiModelProperty(value = "详情url",name = "url")
    private String url;

}