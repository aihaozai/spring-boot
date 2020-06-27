package com.example.myproject.common.FileListener;

import com.example.myproject.common.FTP.FTPSUtil;
import com.example.myproject.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-13 12:41
 **/
@Slf4j
@Component
public class FileListenerAdaptor extends FileAlterationListenerAdaptor {

    private long overTime;

    private String overOpen;

    public void startFileLister(long interval, String folderPath,long overTime,String overOpen) throws Exception {
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval);// 每隔xx毫秒扫描一次
        FileAlterationObserver observer = new FileAlterationObserver(folderPath, new FileFilterImpl());
        FileListenerAdaptor listenerAdaptor = new FileListenerAdaptor();
        listenerAdaptor.overTime = overTime;
        listenerAdaptor.overOpen = overOpen;
        observer.addListener(listenerAdaptor);
        monitor.addObserver(observer);
        monitor.start();
        FileMonitorUtil.setMonitor(monitor,true);
        log.info("FTP监测文件上传已启动...");
    }
    /**
     * File system observer started checking event.
     */
    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
        if(System.currentTimeMillis()-FileMonitorUtil.startTime>overTime){
            try {
                if(overOpen.equals("ON")){
                    FileMonitorUtil.stop();
                    log.warn("由于超过"+overTime+"毫秒没有新文件进入,FTP监测服务已关闭");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //log.info("文件系统观察者开始检查事件");
    }
    /**
     * File system observer finished checking event.
     */
    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
        //log.info("文件系统完成检查事件观测器");
    }

    /**
     * Directory created Event.
     */
    @Override
    public void onDirectoryCreate(File directory) {
        super.onDirectoryCreate(directory);
        //log.info("目录创建事件");
    }

    /**
     * Directory changed Event
     */
    @Override
    public void onDirectoryChange(File directory) {
        super.onDirectoryChange(directory);
        //log.info("目录改变事件");
    }

    /**
     * Directory deleted Event.
     */
    @Override
    public void onDirectoryDelete(File directory) {
        super.onDirectoryDelete(directory);
        //log.info("目录删除事件");
    }

    /**
     * File created Event.
     */
    @Override
    public void onFileCreate(File file) {
        super.onFileCreate(file);
        log.info("新文件进入>>>准备上传FTP服务器");
        FTPSUtil ftpsUtil = new FTPSUtil(PropertiesUtil.getProperty("system.ftp.host"),Integer.parseInt(PropertiesUtil.getProperty("system.ftp.port")),PropertiesUtil.getProperty("system.ftp.username"),PropertiesUtil.getProperty("system.ftp.password"));
        try {
            ftpsUtil.uploadFileByFile(PropertiesUtil.getProperty("system.ftp.pathname"),file.getName(),file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        //log.info("文件名称：" + file.getName());
    }

    /**
     * File changed Event.
     */
    @Override
    public void onFileChange(File file) {
        super.onFileChange(file);
        //log.info("文件改变事件");
    }

    /**
     * File deleted Event.
     */
    @Override
    public void onFileDelete(File file) {
        super.onFileDelete(file);
        //log.info("文件删除事件:" + file.getName());
    }

}
