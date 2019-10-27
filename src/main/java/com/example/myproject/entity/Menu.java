package com.example.myproject.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
public class Menu implements Serializable {
    private static final long serialVersionUID = 800335886544606075L;
    @Id
    private String id;

    private String pid;
    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_type")
    private String menuType;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_icon")
    private String menuIcon;

    private Integer sort;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private String updateTime;

}
