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
 * @Create: 2019-09-19 15:18
 **/
@Getter
@Setter
@Entity
@Table(name = "menu_permission")
@ApiModel(value="MenuPermission表",description = "菜单访问权限对象")
public class MenuPermission implements Serializable {

    private static final long serialVersionUID = -2839342315167639399L;

    @Id
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id;

    @ApiModelProperty(value = "菜单id",name="menuId")
    @Column(name = "menu_id")
    private String menuId;

    @ApiModelProperty(value = "菜单名称",name="menuName")
    @Column(name = "menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单权限名称",name="permissionName")
    @Column(name = "permission_name")
    private String permissionName;

    @ApiModelProperty(value = "菜单权限代码",name="permission")
    private String permission;

}
