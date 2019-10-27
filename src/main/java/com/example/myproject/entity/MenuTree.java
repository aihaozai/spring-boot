package com.example.myproject.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-09 15:33
 **/
@Getter
@Setter
public class MenuTree implements Serializable{
    private static final long serialVersionUID = -1280772062912645554L;
    private String id;
    private String pid;
    private String menuId;
    private String menuName;
    private String menuType;
    private String menuUrl;
    private String menuIcon;
    private String sort;
    private List<MenuTree> childrens = new ArrayList<>();
}
