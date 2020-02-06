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
 * @Create: 2019-09-19 15:18
 **/
@Getter
@Setter
@Entity
@Table(name = "menu_permission")
public class MenuPermission implements Serializable {

    private static final long serialVersionUID = -2839342315167639399L;

    @Id
    private String id;

    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "permission_name")
    private String permissionName;

    private String permission;

}
