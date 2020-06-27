package com.example.myproject.mapper;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.sys.Organ;

import java.util.List;

public interface OrganMapper {

    List<Organ> findAll(JSONObject jsonObject);


}