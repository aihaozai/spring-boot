package com.example.myproject.service.business;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.business.ApplyLeave;
import com.example.myproject.common.pojo.SystemResponse;

import java.util.Map;

public interface IApplyLeaveService {
    Map findListByPage(Page page);

    SystemResponse addOrUpdateApplyLeave(ApplyLeave applyLeave);

    ApplyLeave findById(String id);

    SystemResponse checkProcess(String id, String type) throws Exception;

    void completeProcess(String tableId, String type) throws Exception;
}
