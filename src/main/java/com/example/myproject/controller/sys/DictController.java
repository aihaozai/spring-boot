package com.example.myproject.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.entity.sys.Dict;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-01-02 12:49
 **/
@Api(tags = "字典接口")
@Controller
@RequestMapping("dict")
public class DictController {

    @Autowired
    private IDictService dictServiceImpl;

    @ApiOperation("字典页面接口")
    @RequestMapping("/dictList")
    public String dictList(){
        return "sys/dict/dictList";
    }

    @ApiOperation("字典分页接口")
    @ResponseBody
    @GetMapping("/getDictPage")
    public SystemResponse getDictPage(Page page){
        try {
            Map map =  dictServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("保存更新字典接口")
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

    @ApiOperation("删除字典接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictId", value = "字典id",required = true),
            @ApiImplicitParam(name = "target", value = "是否是父字典",required = true)
    })
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
