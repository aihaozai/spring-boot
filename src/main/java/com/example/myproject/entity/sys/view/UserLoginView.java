package com.example.myproject.entity.sys.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "user_login_view")
@ApiModel(value="UserLoginView视图",description = "用户登录视图对象")
public class UserLoginView implements Serializable {

    private static final long serialVersionUID = -1933600250049197625L;
    @Id
    @ApiModelProperty(value = "用户id",name = "id")
    private String id;

    @ApiModelProperty(value = "账号",name = "account")
    private String account;

    @ApiModelProperty(value = "密码",name = "password")
    private String password;

    @ApiModelProperty(value = "用户名称",name = "username")
    private String username;

    @ApiModelProperty(value = "用户角色id",name = "username")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "用户所属机构id",name = "username")
    @Column(name = "organ_id")
    private String organId;

    @ApiModelProperty(value = "是否锁定",name = "statusLock")
    @Column(name = "status_lock")
    private Integer statusLock;

    @ApiModelProperty(value = "用户所属机构名称",name = "username")
    @Column(name = "organ_name")
    private String organName;

}