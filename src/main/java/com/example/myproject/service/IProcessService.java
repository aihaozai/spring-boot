package com.example.myproject.service;

import com.example.myproject.entity.Page;
import java.util.Map;

public interface IProcessService {

    Map findListByPage(Page page);
}
