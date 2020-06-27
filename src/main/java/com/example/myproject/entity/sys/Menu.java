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

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-01 20:57
 **/
@Getter
@Setter
@Entity
@Table(name = "menu")
@ApiModel(value="Menu表",description = "菜单对象")
public class Menu implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    @ApiModelProperty(value = "主键",name = "id",required = true)
    private String id;

    @ApiModelProperty(value = "上级菜单id",name = "pid")
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

    @ApiModelProperty(value = "访问菜单路径",name = "menuUrl")
    @Column(name = "menu_url")
    private String menuUrl;

    @ApiModelProperty(value = "菜单图标",name = "menuIcon")
    @Column(name = "menu_icon")
    private String menuIcon;

    @ApiModelProperty(value = "排序",name = "sort",example = "1")
    private Integer sort;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(value = "更新时间",name = "updateTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}
