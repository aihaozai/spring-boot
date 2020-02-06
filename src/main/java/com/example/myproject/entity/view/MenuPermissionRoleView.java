package com.example.myproject.entity.view;

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
public class MenuPermissionRoleView {
    @Id
    private String id;
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "menu_id")
    private String menuId;
    @Column(name = "role_permission")
    private String rolePermission;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "permission_name")
    private String permissionName;
    private String permission;
}
