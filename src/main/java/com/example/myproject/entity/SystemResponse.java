package com.example.myproject.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-20 15:56
 **/
public class SystemResponse extends JSONObject {
    private static final long serialVersionUID = -4779671740130436649L;
    public SystemResponse code(String code) {
        this.put("code", code);
        return this;
    }

    public SystemResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public SystemResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    public SystemResponse count(Object count) {
        this.put("count", count);
        return this;
    }
    public SystemResponse success() {
        this.code("success");
        return this;
    }

    public SystemResponse fail() {
        this.code("fail");
        return this;
    }

    public SystemResponse error() {
        this.message("系统错误");
        return this;
    }
    public SystemResponse pageData(Object count,Object data) {
        this.code("0");
        this.message("success");
        this.count(count);
        this.data(data);
        return this;
    }
    public SystemResponse pageDataFail() {
        this.code("1");
        this.message("系统错误");
        this.count("0");
        this.data(new ArrayList<>());
        return this;
    }
}
