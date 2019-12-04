package com.example.myproject.common.FileListener;

import com.example.myproject.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-13 13:27
 **/
@Slf4j
@Component
public class FileCommandLineRunner implements CommandLineRunner {
    @Autowired
    private FileListenerAdaptor listenerAdaptor;
    @Override
    public void run(String... args) throws Exception {
        if(PropertiesUtil.getProperty("system.listener.open").equals("ON")){
            listenerAdaptor.startFileLister(Long.parseLong(PropertiesUtil.getProperty("system.listener.time")),PropertiesUtil.getProperty("system.listener.path"),Long.parseLong(PropertiesUtil.getProperty("system.listener.overtime")),PropertiesUtil.getProperty("system.listener.overopen"));
        }
    }
}
