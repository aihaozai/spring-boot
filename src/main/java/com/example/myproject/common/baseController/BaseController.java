package com.example.myproject.common.baseController;

import com.example.myproject.common.HandlerInterceptor.WebSocketHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-16 14:44
 **/
public class BaseController extends WebSocketHandler {
    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }
}
