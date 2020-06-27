package com.example.myproject.common.shiro;

import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.sys.view.UserLoginView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.io.Serializable;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-02-22 14:33
 **/
public class SystemSessionIdGenerator implements SessionIdGenerator{
    @Override
    public Serializable generateId(Session session) {
        UserLoginView userLoginView = (UserLoginView)SecurityUtils.getSubject().getPrincipal();
        //if(session.getId()==null)return "";
        //SimplePrincipalCollection coll = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        //UserLoginView userLoginView = (UserLoginView)coll.getPrimaryPrincipal();
        return userLoginView.getId();
    }
}
