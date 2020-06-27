package com.example.myproject.common.pojo;

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
        //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
        // 实体类中使用@JSONField转时间要把结果转JSON 所以使用@JsonFormat最好,不用转
        //this.data(JSONObject.toJSON(data));
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
    public SystemResponse addMsgSuccess() {
        this.put("message", "添加成功");
        return this;
    }
    public SystemResponse delMsgSuccess() {
        this.put("message", "删除成功");
        return this;
    }
    public SystemResponse delMsgFail() {
        this.put("message", "删除失败");
        return this;
    }
    public SystemResponse updateMsgSuccess() {
        this.put("message", "修改成功");
        return this;
    }
    public SystemResponse updateMsgFail() {
        this.put("message", "修改失败");
        return this;
    }

    public SystemResponse approveSuccess(){
        this.put("message", "发起审批成功");
        return this;
    }
}
