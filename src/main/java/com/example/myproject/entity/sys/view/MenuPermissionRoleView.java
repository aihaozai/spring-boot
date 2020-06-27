package com.example.myproject.entity.sys.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-24 17:58
 **/
@Getter
@Setter
@Entity
@Table(name = "menu_permission_role_view")
@ApiModel(value="MenuPermissionRoleView视图",description = "角色菜单权限视图对象")
public class MenuPermissionRoleView {
    @Id
    @ApiModelProperty(value = "permission_role表主键",name = "id")
    private String id;

    @ApiModelProperty(value = "角色id",name = "roleId")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty(value = "菜单id",name = "menuId")
    @Column(name = "menu_id")
    private String menuId;

    @ApiModelProperty(value = "菜单操作权限id",name = "menuId")
    @Column(name = "role_permission")
    private String rolePermission;

    @ApiModelProperty(value = "菜单名称",name = "menuId")
    @Column(name = "menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单操作权限名称",name = "menuId")
    @Column(name = "permission_name")
    private String permissionName;

    @ApiModelProperty(value = "菜单操作权限代码",name = "menuId")
    private String permission;
}
