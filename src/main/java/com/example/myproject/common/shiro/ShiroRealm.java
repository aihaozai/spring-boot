package com.example.myproject.common.shiro;

import com.example.myproject.entity.MenuPermission;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.service.IMenuPermissionService;
import com.example.myproject.service.IShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-16 16:02
 **/
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private IShiroService shiroServiceImpl;
    /**
     *@author Jen
     *@Point  授权
     *@Param  principal
     *@return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUsername();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 获取用户角色集合
        List<UserRole> userRoles = shiroServiceImpl.findListByFiled("userRole",user.getRoleId());
        Set<String> roleSet = userRoles.stream().map(UserRole::getPersonRole).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);
        // 获取用户权限集合
        List<Object> list = userRoles.stream().map(UserRole::getPersonRole).collect(Collectors.toList());
        List<MenuPermission> permissionList = shiroServiceImpl.findListByFiledIn("roleId",list);
        Set<String> permissionSet = permissionList.stream().map(MenuPermission::getPermission).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }
    /**
     *@author Jen
     *@Point  认证身份
     *@Param  authenticationToken
     *@Exception AuthenticationException
     *@return AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String userName = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        // 通过用户名到数据库查询用户信息
        User user = shiroServiceImpl.findByFiled("account",userName);
        if (user == null)
            throw new UnknownAccountException("用户名或密码错误！");
        if (!StringUtils.equals(password, user.getPassword()))
            throw new IncorrectCredentialsException("用户名或密码错误！");
        if (User.STATUS_LOCK.equals(user.getStatusLock()))
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}

