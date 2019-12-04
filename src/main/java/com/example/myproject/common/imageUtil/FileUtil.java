package com.example.myproject.common.imageUtil;

import java.io.File;
import java.io.IOException;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-28 12:29
 **/
public class FileUtil {
    public static File getFile(String fileName)throws IOException {
        File file = new File("F:/reptile/"+fileName+".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
