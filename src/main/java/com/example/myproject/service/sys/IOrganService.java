package com.example.myproject.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.entity.sys.Organ;
import com.example.myproject.common.pojo.Page;

import java.util.List;
import java.util.Map;

public interface IOrganService {
    Map findListByPage(Page page);

    List<String> getRoleOrgan(String tField, String field, Object object);

    String addOrUpdateOrgan(Organ organ);

    void delOrgan(String organId);

    String authOrgan(String roleId, String roleName, String organIds);

    List<Organ> findAll();

    Organ findById(String id);

    Integer updateOrganById(String id, String field, String value) throws Exception;

    List findAll(JSONObject data);
}
