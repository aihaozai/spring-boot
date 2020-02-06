package com.example.myproject.service;

import com.example.myproject.entity.view.UserOrganRoleView;

import java.util.List;

public interface IApproveService {
    List<UserOrganRoleView> findLeader(String organId,String level);
}
