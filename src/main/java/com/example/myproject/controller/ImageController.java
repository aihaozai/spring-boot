package com.example.myproject.controller;

import com.example.myproject.entity.SystemResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-27 15:03
 **/
@Controller
@RequestMapping("/image")
public class ImageController {

    @RequestMapping("/imageList")
    public  String imageList(){
        return "image/imageList";
    }
    @ResponseBody
    @RequestMapping("/getImageUrl")
    public SystemResponse getImageUrl(String type){
        String[] array = new String[]{"F:/fengjing.txt","F:/meizi.txt"};
        String pathName = array[Integer.parseInt(type)];
        List<String> list = new ArrayList();
        try {
            FileReader fileReader = new FileReader(pathName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 一次读入一行数据
                list.add(line);
            }
            return new SystemResponse().success().message("读取图片Url成功").data(list);
        }catch (Exception e){
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
}
