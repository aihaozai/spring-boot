package com.example.myproject.common.config;

import org.activiti.engine.impl.cfg.IdGenerator;

import java.util.UUID;

/**
 * @Auther: Ace Lee
 * @Date: 2019/3/11 16:05
 */
public class MyIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}