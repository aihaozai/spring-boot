package com.example.myproject.entity.view;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ApplyLeaveView implements Serializable {
    private static final long serialVersionUID = 2153768014181807756L;
    @Id
    private String id;

    @Column(name = "apply_user")
    private String applyUser;

    private String username;

    @Column(name = "user_organ")
    private String userOrgan;

    @Column(name = "organ_name")
    private String organName;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "approve_status")
    private String approveStatus;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "apply_reason")
    private String applyReason;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "start_time")
    private String startTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "end_time")
    private String endTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd",timezone="GMT+8")
    @Column(name = "create_time")
    private Date createTime;
}
