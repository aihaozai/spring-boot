package com.example.myproject.entity.activiti.view;

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
@Table(name = "apply_leave_view")
@ApiModel(value="ApplyLeaveView视图",description = "请假视图对象")
public class ApplyLeaveView implements Serializable {
    private static final long serialVersionUID = 2153768014181807756L;
    @Id
    @ApiModelProperty(value = "主键",name = "id",required = true)
    private String id;

    @ApiModelProperty(value = "申请用户id",name = "applyUser")
    @Column(name = "apply_user")
    private String applyUser;

    @ApiModelProperty(value = "申请用户名称",name = "username")
    private String username;

    @ApiModelProperty(value = "用户机构id",name = "userOrgan")
    @Column(name = "user_organ")
    private String userOrgan;

    @ApiModelProperty(value = "用户机构名称",name = "organName")
    @Column(name = "organ_name")
    private String organName;

    @ApiModelProperty(value = "请假类型id",name = "leaveType")
    @Column(name = "leave_type")
    private String leaveType;

    @ApiModelProperty(value = "请假类型名称",name = "typeName")
    @Column(name = "type_name")
    private String typeName;

    @ApiModelProperty(value = "审批状态",name = "approveStatus")
    @Column(name = "approve_status")
    private String approveStatus;

    @ApiModelProperty(value = "审批状态名称",name = "statusName")
    @Column(name = "status_name")
    private String statusName;

    @ApiModelProperty(value = "申请原因",name = "applyReason")
    @Column(name = "apply_reason")
    private String applyReason;

    @ApiModelProperty(value = "开始时间",name = "startTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "start_time")
    private String startTime;

    @ApiModelProperty(value = "结束时间",name = "endTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "end_time")
    private String endTime;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd",timezone="GMT+8")
    @Column(name = "create_time")
    private Date createTime;
}
