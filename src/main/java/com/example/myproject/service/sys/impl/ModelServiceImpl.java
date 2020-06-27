package com.example.myproject.service.sys.impl;

import com.example.myproject.common.annotation.TargetDataSource;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.service.sys.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-04 09:58
 **/
@Service
public class ModelServiceImpl implements IModelService {

    @Autowired
    private AllDao.ActReModelDao actReModelDao;

    @TargetDataSource(name = "activiti")
    @Override
    public Map findListByPage(Page page) {
        return actReModelDao.findListByPage(page);
    }
}
