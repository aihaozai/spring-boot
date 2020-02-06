package com.example.myproject.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.Organ;
import com.example.myproject.entity.OrganRole;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.mapper.OrganMapper;
import com.example.myproject.service.IOrganService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-20 16:19
 **/
@Service
public class OrganServiceImpl implements IOrganService {

    @Autowired
    private AllDao.OrganDao organDao;

    @Autowired
    private AllDao.OrganRoleDao organRoleDao;

    @Autowired
    private OrganMapper organMapper;

    @Override
    public Map findListByPage(Page page) {
        return organDao.findListByPage(page);
    }

    @Override
    public List<String> getRoleOrgan(String tField, String field, Object object) {
        return organRoleDao.findListByFiled(tField,field,object);
    }

    @Override
    public String addOrUpdateOrgan(Organ organ) {
        if(StringUtils.isNotEmpty(organ.getId())){
            organ.setUpdateTime(UUIDUtil.currentTime());
            organDao.update(organ);
            return "修改成功";
        }else {
            organ.setId(UUIDUtil.randomUUID());
            organ.setOrganId(UUIDUtil.randomUUID());
            organ.setCreateTime(UUIDUtil.currentTime());
            Integer num = organDao.findMaxByFiled("sort","pid",organ.getPid());
            organ.setSort(num+1);
            organDao.save(organ);
            return "添加成功";
        }
    }

    @Override
    public void delOrgan(String organId) {
        String[] organIds = organId.split(",");
        for (String id:organIds){
            List<String> list = organDao.findListByFiled("organId","pid",id);
            delChildren(list);
            organDao.deleteByFiled("organId",id);
            organRoleDao.deleteByFiled("roleOrgan",id);
        }
    }
    @Transactional
    @Override
    public String authOrgan(String roleId, String roleName, String organIds) {
        List<OrganRole> organRoles = organRoleDao.findListByFiled("roleId",roleId);
        List<String> oldList = organRoles.stream().map(OrganRole::getRoleOrgan).collect(Collectors.toList());
        List<String> newList = Arrays.asList(organIds.split(","));
        List<String> existList = new ArrayList<>();
        for (String id:newList){
            if(oldList.contains(id)){
                existList.add(id);
            }else {
                OrganRole organRole = new OrganRole();
                organRole.setId(UUIDUtil.randomUUID());
                organRole.setRoleId(roleId);
                organRole.setRoleName(roleName);
                organRole.setRoleOrgan(id);
                organRoleDao.save(organRole);
            }
        }
        oldList.removeAll(existList);
        for (Object id:oldList){
            for (OrganRole organRole:organRoles){
                if(organRole.getRoleOrgan().equals(id)){
                    organRoleDao.delete(organRole.getId());
                    break;
                }
            }
        }
        return "授权成功";
    }

    @Override
    public List<Organ> findAll() {
        return organDao.findAll();
    }

    @Override
    public Organ findById(String id) {
        return organDao.findByID(id);
    }

    @Override
    public Integer updateOrganById(String id, String field, String value) throws Exception {
        return organDao.updateById(id,field,value);
    }

    @Override
    public List findAll(JSONObject data) {
        return organMapper.findAll(data);
    }

    private void delChildren(List<String> list){
        for (String childrenId:list){
            List<String> childrenList = organDao.findListByFiled("organId","pid",childrenId);
            delChildren(childrenList);
            organDao.deleteByFiled("organId",childrenId);
            organRoleDao.deleteByFiled("roleOrgan",childrenId);
        }
    }
}
