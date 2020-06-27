package com.example.myproject.service.business.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.ApplyLeave;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.entity.activiti.ActHiTaskProcess;
import com.example.myproject.mapper.ApplyLeaveMapper;
import com.example.myproject.service.IApplyLeaveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 23:06
 **/
@Service
public class ApplyLeaveServiceImpl implements IApplyLeaveService {
    @Autowired
    private AllDao.ApplyLeaveDao applyLeaveDao;

    @Autowired
    private AllDao.ApplyLeaveViewDao applyLeaveViewDao;

    @Autowired
    private ApplyLeaveMapper applyLeaveMapperDao;

    @Autowired
    private AllDao.ActHiTaskProcessDao hiTaskProcessDao;

    @Override
    public Map findListByPage(Page page) {
        return applyLeaveViewDao.findListByPage(page);
    }

    @Override
    public SystemResponse addOrUpdateApplyLeave(ApplyLeave applyLeave) {
        if(StringUtils.isEmpty(applyLeave.getId())){
            String id = UUIDUtil.randomUUID();
            applyLeave.setId(id);
            applyLeave.setCreateTime(new Date());
            applyLeaveDao.save(applyLeave);
            return new SystemResponse().success().addMsgSuccess().data(id);
        }else {
            applyLeaveMapperDao.updateApplyLeave(applyLeave);
            return new SystemResponse().success().updateMsgSuccess();
        }
    }

    @Override
    public ApplyLeave findById(String id) {
        return applyLeaveDao.findByID(id);
    }

    @Override
    public SystemResponse checkProcess(String id, String type) throws Exception {
        Integer num = hiTaskProcessDao.findCountByFiled("tableId",id);
        if(num > 0){
            applyLeaveDao.updateById(id,"approveStatus",type);
            return new SystemResponse().success();
        }else {
            return new SystemResponse().fail();
        }
    }

    @Override
    public void completeProcess(String tableId, String type) throws Exception {
        applyLeaveDao.updateById(tableId,"approveStatus",type);
    }
}
