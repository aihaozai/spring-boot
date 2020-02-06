package com.example.myproject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.Dict;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 12:49
 **/
@Controller
@RequestMapping("dict")
public class DictController {

    @Autowired
    private IDictService dictServiceImpl;

    @RequestMapping("/dictList")
    public String dictList(){
        return "dict/dictList";
    }

    @ResponseBody
    @RequestMapping("/getDictPage")
    public SystemResponse getDictPage(Page page){
        try {
            Map map =  dictServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }
    @ResponseBody
    @PostMapping("/addOrUpdateDictById")
    public SystemResponse addOrUpdateDictById(@RequestBody JSONObject jsonObject){
        try {
            return dictServiceImpl.addOrUpdateDictById(JSON.parseObject(jsonObject.toJSONString(),Dict.class),jsonObject.getString("field"),jsonObject.getString("value"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
    @ResponseBody
    @PostMapping("/delDict")
    public SystemResponse delDict(String id,String target){
        try {
            dictServiceImpl.delDict(id,target);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().delMsgSuccess();
        }
    }
}
