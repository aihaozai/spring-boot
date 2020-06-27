package com.example.myproject.entity.sys;

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
 * @Create: 2019-10-19 11:20
 **/
@Getter
@Setter
@Entity
@Table(name = "permission_role")
@ApiModel(value="PermissionRole表",description = "角色拥有菜单权限对象")
public class PermissionRole implements Serializable {
    private static final long serialVersionUID = 5161144247567983328L;

    @Id
    @ApiModelProperty(value = "主键",name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "角色id",name = "id")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty(value = "菜单id",name = "menuId")
    @Column(name = "menu_id")
    private String menuId;

    @ApiModelProperty(value = "菜单权限id",name = "rolePermission")
    @Column(name = "role_permission")
    private String rolePermission;

}
