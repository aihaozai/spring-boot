package com.example.myproject.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-15 12:51
 **/
@Getter
@Setter
public class Page implements Serializable {
    private static final long serialVersionUID = -5601574509801101671L;
    private Integer limit;
    private Integer page;
    private JSONObject data;
}
