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
 * @Create: 2019-09-01 20:57
 **/
@Getter
@Setter
@Entity
@Table(name = "person_menu_view")
@ApiModel(value="PersonMenuView视图",description = "人员角色菜单视图对象")
public class PersonMenuView implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    @ApiModelProperty(value = "menu_role表主键",name = "id")
    private String id;

    @ApiModelProperty(value = "角色id",name = "roleId")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty(value = "上级id",name = "pid")
    private String pid;

    @ApiModelProperty(value = "菜单id",name = "menuId")
    @Column(name = "menu_id")
    private String menuId;

    @ApiModelProperty(value = "菜单名称",name = "menuName")
    @Column(name = "menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单类型",name = "menuType")
    @Column(name = "menu_type")
    private String menuType;

    @ApiModelProperty(value = "访问菜单url",name = "menuUrl")
    @Column(name = "menu_url")
    private String menuUrl;

    @ApiModelProperty(value = "菜单图标",name = "menuIcon")
    @Column(name = "menu_icon")
    private String menuIcon;

    @ApiModelProperty(value = "排序",name = "sort",example = "0")
    private Integer sort;

}
