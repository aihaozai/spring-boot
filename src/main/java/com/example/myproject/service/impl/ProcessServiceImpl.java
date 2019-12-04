package com.example.myproject.service.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.ActReModel;
import com.example.myproject.entity.Page;
import com.example.myproject.service.IProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-27 16:18
 **/
@Service
public class ProcessServiceImpl implements IProcessService {
    @Autowired
    private AllDao.ActReModelDao actReModelDao;

    @TargetDataSource(name = "activiti")
    @Override
    public Map findListByPage(Page page) {
        return actReModelDao.findListByPage(page);
    }
}
