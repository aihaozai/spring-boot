package com.example.myproject.mapper;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.activiti.ActHiTaskProcess;

import java.util.List;

public interface ActHiTaskProcessMapper {

    List<ActHiTaskProcess> findAll(JSONObject jsonObject);

    Integer getTaskCount(String id, String status);
}