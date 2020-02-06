package com.example.myproject.entity.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ActHiTaskProcess implements Serializable {

    private static final long serialVersionUID = -1637407873763545068L;

    @Id
    private String id;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "process_key")
    private String processKey;

    @Column(name = "table_id")
    private String tableId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "launch_id")
    private String launchId;

    private String assignee;

    private String status;

    @Column(name = "agree_status")
    private Integer agreeStatus;

    private String opinion;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "start_time")
    private Timestamp startTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "end_time")
    private Timestamp endTime;

    private String url;

}