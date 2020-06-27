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

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-06 12:04
 **/
@Getter
@Setter
@Entity
@Table(name = "user_organ_role_view")
@ApiModel(value="UserOrganRoleView视图",description = "用户角色机构视图对象")
public class UserOrganRoleView implements Serializable {
    private static final long serialVersionUID = -6263583581683080437L;

    @Id
    @ApiModelProperty(value = "用户id",name = "id",required = true)
    private String id;

    @ApiModelProperty(value = "用户名称",name = "username")
    private String username;

    @ApiModelProperty(value = "上级机构id",name = "organId")
    @Column(name = "organ_id")
    private String organId;

    @ApiModelProperty(value = "用户角色id",name = "userRole")
    @Column(name = "user_role")
    private String userRole;

    @ApiModelProperty(value = "角色id",name = "roleId")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty(value = "机构id",name = "roleOrgan")
    @Column(name = "role_organ")
    private String roleOrgan;

    @ApiModelProperty(value = "机构等级",name = "organLevel")
    @Column(name = "organ_level")
    private String organLevel;
}
