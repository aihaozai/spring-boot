package com.example.myproject.service.business.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.entity.view.UserOrganRoleView;
import com.example.myproject.service.IApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-06 12:07
 **/
@Service
public class ApproveServiceImpl implements IApproveService {
    @Autowired
    private AllDao.UserOrganRoleViewDao userOrganRoleViewDao;

    /**
     *@author Jen
     *@Description  查询拥有当前用户机构的
     *@Param organId 用户机构id , level 当前流程等级
     *@return List<UserOrganRoleView>
     */
    @Override
    public List<UserOrganRoleView> findLeader(String organId,String level) {
        //获取拥有该机构的角色
        List<UserOrganRoleView> roleViews = userOrganRoleViewDao.findListByFiled("roleOrgan",organId);
        List<UserOrganRoleView> result = new ArrayList<>();
        for(UserOrganRoleView userOrganRoleView : roleViews){
            //判断该角色是否拥有该流程机构等级
            String sql = MessageFormat.format("select count(*) from UserOrganRoleView where roleId = ''{0}'' and organLevel = ''{1}''",userOrganRoleView.getRoleId(),Integer.parseInt(level));
            Integer count = Integer.parseInt(userOrganRoleViewDao.findByHSQL(sql).toString());
            if(count>0){
                //判断当前角色是否含有上一个等级
                sql = MessageFormat.format("select count(*) from UserOrganRoleView where roleId = ''{0}'' and organLevel <= ''{1}''",userOrganRoleView.getRoleId(),Integer.parseInt(level)-1);
                count = Integer.parseInt(userOrganRoleViewDao.findByHSQL(sql).toString());
                if(count==0){
                    result.add(userOrganRoleView);
                }
            }
        }
        return result;
    }
}
