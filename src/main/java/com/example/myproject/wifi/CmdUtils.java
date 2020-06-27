package com.example.myproject.wifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2020-04-06 20:51
 **/
public class CmdUtils {


    /**
     * 该方法封装了执行cmd的方法
     *
     * @param cmd CMD命令
     * @param filePath 需要在哪个目录下执行
     */
    public static List<String> execute(String cmd, String filePath) {
        Process process = null;
        List<String> result = new ArrayList<String>();
        try {
            if (filePath != null) {
                process = Runtime.getRuntime().exec(cmd, null, new File(filePath));
            } else {
                process = Runtime.getRuntime().exec(cmd);
            }
            BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
