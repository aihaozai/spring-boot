package com.example.myproject.entity;

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
public class PermissionRole implements Serializable {
    private static final long serialVersionUID = 5161144247567983328L;

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

}
