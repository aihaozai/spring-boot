package com.example.myproject.service.impl;

import com.example.myproject.common.baseDao.AllDao;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.utils.UUIDUtil;
import com.example.myproject.entity.Dict;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 12:50
 **/
@Service
public class DictServiceImpl implements IDictService {
    @Autowired
    private AllDao.DictDao dictDao;

    @Override
    public Map findListByPage(Page page) {
        return dictDao.findListByPage(page);
    }

    @Override
    public SystemResponse addOrUpdateDictById(Dict dict,String field,String value) throws Exception {
        if(StringUtils.isEmpty(dict.getId())){
            dict.setId(UUIDUtil.randomUUID());
            dict.setDictId(UUIDUtil.randomUUID());
            Integer num = dictDao.findMaxByFiled("sort","pid",dict.getPid());
            dict.setSort(num+1);
            dict.setCreateTime(UUIDUtil.currentTime());
            dictDao.save(dict);
            return new SystemResponse().success().addMsgSuccess();
        }else {
            HashMap<String,Object> hashMap = new HashMap<>();
            if(StringUtils.isEmpty(field)){
                hashMap.put("dictName",dict.getDictName());
                hashMap.put("dictCode",dict.getDictCode());
            }else {
                hashMap.put(field,value);
            }
            hashMap.put("updateTime",UUIDUtil.currentTime());
            dictDao.updateById(dict.getId(),hashMap);
            if(dict.getPid().equals("0")){
                dictDao.updateByFiled("pname",dict.getDictName(),"pid",dict.getDictId());
            }
            return new SystemResponse().success().updateMsgSuccess();
        }
    }

    @Override
    public void delDict(String id, String target) {
        dictDao.deleteByFiled("dictId",id);
        if(target.equals("parent")){
            dictDao.deleteByFiled("pid",id);
        }
    }

    @Override
    public List<Dict> findDictByPName(String pname) {
        return dictDao.findListByFiled("pname",pname);
    }
}
