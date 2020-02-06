package com.example.myproject.mapper;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.Organ;
import com.example.myproject.entity.activiti.ActHiTaskProcess;

import java.util.List;

public interface OrganMapper {

    List<Organ> findAll(JSONObject jsonObject);


}