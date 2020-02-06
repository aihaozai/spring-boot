package com.example.myproject.service;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.Dict;
import com.example.myproject.entity.SystemResponse;

import java.util.List;
import java.util.Map;

public interface IDictService {
    Map findListByPage(Page page);

    SystemResponse addOrUpdateDictById(Dict dict,String field,String value) throws Exception;

    void delDict(String id, String target);

    List<Dict> findDictByPName(String pname);
}
