package com.example.myproject.service;

import com.example.myproject.common.pojo.Page;
import java.util.Map;

public interface IProcessService {

    Map findListByPage(Page page);
}
