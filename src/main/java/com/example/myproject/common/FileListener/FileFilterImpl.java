package com.example.myproject.common.FileListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-13 13:01
 **/
@Slf4j
@Component
public class FileFilterImpl implements FileFilter {
    /**
     * @return return false:返回主目录下所有文件详细(不包含所有子目录)
     */
    @Override
    public boolean accept(File file) {
        //log.info("文件路径: " + file);
        //log.info("最后修改时间： " + file.lastModified());
        return true;
    }
}
