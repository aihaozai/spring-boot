package com.example.myproject.service.sys;

import com.example.myproject.entity.sys.view.UserOrganRoleView;

import java.util.List;

public interface IApproveService {
    List<UserOrganRoleView> findLeader(String organId,String level);
}
