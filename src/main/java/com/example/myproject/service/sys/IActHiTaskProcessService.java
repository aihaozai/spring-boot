package com.example.myproject.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.activiti.ActHiTaskProcess;

import java.util.List;

public interface IActHiTaskProcessService {
    List<ActHiTaskProcess> findAll(JSONObject jsonObject);

    Integer getTaskCount(String id, String count);

    ActHiTaskProcess findByTableId(String taskId);

    void completeActHiTaskProcess(String taskId,String opinion,Integer agreeStatus);

    void saveActHiTaskProcess(ActHiTaskProcess actHiTaskProcess);
}
