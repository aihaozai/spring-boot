package com.example.myproject.service.sys;

import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.sys.Dict;
import com.example.myproject.common.pojo.SystemResponse;

import java.util.List;
import java.util.Map;

public interface IDictService {
    Map findListByPage(Page page);

    SystemResponse addOrUpdateDictById(Dict dict,String field,String value) throws Exception;

    void delDict(String id, String target);

    List<Dict> findDictByPName(String pname);
}
