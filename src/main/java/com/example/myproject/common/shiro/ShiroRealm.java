package com.example.myproject.common.shiro;

import com.example.myproject.entity.sys.User;
import com.example.myproject.entity.sys.UserRole;
import com.example.myproject.entity.sys.view.MenuPermissionRoleView;
import com.example.myproject.entity.sys.view.UserLoginView;
import com.example.myproject.service.sys.IShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
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
    @Autowired
    private SessionDAO sessionDAO;
    /**
     *@author Jen
     *@Point  授权
     *@Param  principal
     *@return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        UserLoginView user = (UserLoginView) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 获取用户角色集合
        List<UserRole> userRoles = shiroServiceImpl.findListByFiled("userRole",user.getRoleId());
        Set<String> roleSet = userRoles.stream().map(UserRole::getPersonRole).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);
        // 获取用户权限集合
        if(roleSet.size()>0){
            List<String> list = userRoles.stream().map(UserRole::getPersonRole).collect(Collectors.toList());
            List<MenuPermissionRoleView> menuPermissionRoleViews = shiroServiceImpl.findListByFiledIn("roleId",list);
            Set<String> permissionSet = menuPermissionRoleViews.stream().map(MenuPermissionRoleView::getPermission).collect(Collectors.toSet());
            simpleAuthorizationInfo.setStringPermissions(permissionSet);
        }
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
        String account = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
        // 通过用户名到数据库查询用户信息
        UserLoginView user = shiroServiceImpl.findByFiled("account",account);
        Collection<Session> sessions = sessionDAO.getActiveSessions();
       if (user == null)
            throw new UnknownAccountException("用户名或密码错误！");
        if (!StringUtils.equals(password, user.getPassword()))
            throw new IncorrectCredentialsException("用户名或密码错误！");
        if (User.STATUS_LOCK.equals(user.getStatusLock()))
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        for (Session session : sessions) {
            SimplePrincipalCollection coll = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if(coll==null)continue;
            UserLoginView userLoginView = (UserLoginView)coll.getPrimaryPrincipal();
            if(user.getId().equals(userLoginView.getId())){
                throw new IncorrectCredentialsException(session.getId().toString());
            }
        }
        user.setPassword(String.valueOf(usernamePasswordToken.isRememberMe()));
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}

