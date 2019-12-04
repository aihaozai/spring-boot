package com.example.myproject.common.FileListener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-15 15:28
 **/
@Slf4j
public class FileMonitorUtil {
    public static FileAlterationMonitor monitor;
    public static long startTime;
    public static boolean running;

    public static void setMonitor(FileAlterationMonitor monitor,boolean running) {
        FileMonitorUtil.monitor = monitor;
        FileMonitorUtil.startTime = System.currentTimeMillis();
        FileMonitorUtil.running = running;
    }

    public static void start() throws Exception {
        FileMonitorUtil.monitor.start();
        FileMonitorUtil.startTime = System.currentTimeMillis();
        FileMonitorUtil.running = true;
        log.info("FTP监测文件上传已启动...");
    }

    public static void stop() throws Exception {
        FileMonitorUtil.monitor.stop();
        FileMonitorUtil.running = false;
    }
}
