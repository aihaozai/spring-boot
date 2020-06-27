package com.example.myproject.service.business.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.User;
import com.example.myproject.entity.UserRole;
import com.example.myproject.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-01 13:14
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AllDao.UserDao userDao;
    @Autowired
    private AllDao.UserRoleDao userRoleDao;

    @Override
    public List<UserRole> findListByFiled(String filed, Object object) {
        return userRoleDao.findListByFiled(filed, object);
    }

    @Override
    public Map findListByPage(Page page) {
        return userDao.findListByPage(page);
    }

    @Override
    public User findById(String id) {
        return userDao.findByID(id);
    }

    @Override
    @Transactional
    public String addOrUpdateUser(User user) {
        if (StringUtils.isNotEmpty(user.getId())) {
            user.setUpdateTime(UUIDUtil.currentTime());
            userDao.update(user);
            return "更新成功";
        } else {
            user.setId(UUIDUtil.randomUUID());
            user.setRoleId(UUIDUtil.randomUUID());
            user.setCreateTime(UUIDUtil.currentTime());
            userDao.save(user);
            return "添加成功";
        }
    }

    @Override
    public String checkUser(String account) {
        User user = userDao.findSingleBeanByFiled("account", account);
        if (user == null) {
            return "success";
        }
        return "fail";
    }

    @Override
    @Transactional
    public String deleteUser(String id, String roleId) {
        userDao.delete(id);
        userRoleDao.deleteByFiled("userRole",roleId);
        return "删除成功";
    }

    @Override
    @Transactional
    public String updateStatusLock(String id, String statusLock) throws Exception{
        userDao.updateById(id,"statusLock",statusLock);
        return statusLock.equals("0")? "锁定" : "有效";
    }
}
