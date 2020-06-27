package com.example.myproject.entity.sys;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user")
@ApiModel(value="User表",description = "用户对象")
public class User implements Serializable {

    private static final long serialVersionUID = -1933600250049197625L;
    // 用户状态：锁定
    public static final Integer STATUS_LOCK = 0;

    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "账号",name = "account")
    private String account;

    @ApiModelProperty(value = "密码",name = "password")
    private String password;

    @ApiModelProperty(value = "用户名称",name = "username")
    private String username;

    @ApiModelProperty(value = "机构id",name = "organId")
    @Column(name = "organ_id")
    private String organId;

    @ApiModelProperty(value = "是否锁定",name = "statusLock")
    @Column(name = "status_lock")
    private Integer statusLock;

    @ApiModelProperty(value = "角色id",name = "roleId")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "更新时间",name = "account")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}