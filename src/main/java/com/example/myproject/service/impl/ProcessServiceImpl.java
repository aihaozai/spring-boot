package com.example.myproject.service.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.service.IProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-27 16:18
 **/
@Service
public class ProcessServiceImpl implements IProcessService {
    @Autowired
    private AllDao.DeplotmentProcdefViewDao deplotmentProcdefViewDao;

    @TargetDataSource(name = "activiti")
    @Override
    public Map findListByPage(Page page) {
        return deplotmentProcdefViewDao.findListByPage(page);
    }

}
