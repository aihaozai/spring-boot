package com.example.myproject.service.sys;

import com.example.myproject.common.pojo.Page;

import java.util.Map;

public interface IModelService {
    Map findListByPage(Page page);
}
