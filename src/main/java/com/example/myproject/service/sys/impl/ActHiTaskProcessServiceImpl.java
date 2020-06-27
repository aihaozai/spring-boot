package com.example.myproject.service.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.mapper.ActHiTaskProcessMapper;
import com.example.myproject.service.sys.IActHiTaskProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-14 11:43
 **/

@Service
public class ActHiTaskProcessServiceImpl implements IActHiTaskProcessService {
    @Autowired
    private ActHiTaskProcessMapper actHiTaskProcessMapper;

    @Autowired
    private AllDao.ActHiTaskProcessDao actHiTaskProcessDao;

    @Override
    public List<ActHiTaskProcess> findAll(JSONObject jsonObject) {
        return actHiTaskProcessMapper.findAll(jsonObject);
    }

    @Override
    public Integer getTaskCount(String id, String status) {
        return actHiTaskProcessMapper.getTaskCount(id,status);
    }

    @Override
    public ActHiTaskProcess findByTableId(String taskId) {
        return actHiTaskProcessDao.findByID(taskId);
    }

    @Override
    public void completeActHiTaskProcess(String taskId,String opinion,Integer agreeStatus) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status","Y");
        hashMap.put("agreeStatus",agreeStatus);
        hashMap.put("opinion",opinion);
        hashMap.put("endTime",new Timestamp(new Date().getTime()));
        actHiTaskProcessDao.updateById(taskId,hashMap);

    }

    @Override
    public void saveActHiTaskProcess(ActHiTaskProcess actHiTaskProcess) {
        actHiTaskProcessDao.save(actHiTaskProcess);
    }
}
