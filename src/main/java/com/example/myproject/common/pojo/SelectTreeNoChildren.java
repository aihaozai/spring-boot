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
public class SelectTreeNoChildren implements Serializable {
    private static final long serialVersionUID = -1614525328899748681L;
    private String id;
    private String name;
    private boolean open = false;
}
