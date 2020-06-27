package com.example.myproject.common.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-01 12:20
 **/
@Getter
@Setter
public class SelectTree implements Serializable {
    private static final long serialVersionUID = -1280772062912645554L;
    private String id;
    private String name;
    private boolean open = false;
    private List<SelectTree> children = new ArrayList<>();
}
