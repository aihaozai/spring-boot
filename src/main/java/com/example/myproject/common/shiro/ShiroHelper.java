package com.example.myproject.common.shiro;

import com.example.myproject.common.annotation.Helper;
import org.apache.shiro.authz.AuthorizationInfo;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-16 16:01
 **/
@Helper
public class ShiroHelper extends ShiroRealm {

    /**
     * 获取当前用户的角色和权限集合
     * @return AuthorizationInfo
     */
    public AuthorizationInfo getCurrentUserAuthorizationInfo() {
        return super.doGetAuthorizationInfo(null);
    }
}
