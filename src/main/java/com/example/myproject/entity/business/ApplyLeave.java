package com.example.myproject.entity.business;

import com.alibaba.fastjson.annotation.JSONField;
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
import java.util.Date;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 23:14
 **/
@Getter
@Setter
@Entity
@Table(name = "apply_leave")
@ApiModel(value="ApplyLeave表",description = "请假对象")
public class ApplyLeave implements Serializable {
    private static final long serialVersionUID = 2153768014181807756L;
    @Id
    @ApiModelProperty(value = "主键",name = "id",required = true)
    private String id;

    @ApiModelProperty(value = "申请用户id",name = "applyUser")
    @Column(name = "apply_user")
    private String applyUser;

    @ApiModelProperty(value = "用户机构",name = "userOrgan")
    @Column(name = "user_organ")
    private String userOrgan;

    @ApiModelProperty(value = "请假类型",name = "leaveType")
    @Column(name = "leave_type")
    private String leaveType;

    @ApiModelProperty(value = "审批状态",name = "approveStatus")
    @Column(name = "approve_status")
    private String approveStatus;

    @ApiModelProperty(value = "申请原因",name = "applyReason")
    @Column(name = "apply_reason")
    private String applyReason;

    @ApiModelProperty(value = "开始时间",name = "startTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "start_time")
    private Date startTime;

    @ApiModelProperty(value = "结束时间",name = "endTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "end_time")
    private Date endTime;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd",timezone="GMT+8")
    @Column(name = "create_time")
    private Date createTime;
}
