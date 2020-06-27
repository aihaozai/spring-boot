package com.example.myproject.controller.sys;

import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.common.utils.imageUtil.JDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.FileReader;
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
    @RequestMapping("/imageAllList")
    public  String imageAllList(){
        return "image/imageAllList";
    }
    @ResponseBody
    @RequestMapping("/getImageUrl")
    public SystemResponse getImageUrl(String url,String type){

        try {
            if(Integer.parseInt(type)==0){
                String head = "https://img30.360buyimg.com/sku/";
                return new SystemResponse().success().message("读取图片Url成功").data(JDUtil.getClassification(url,head));
            }
            return new SystemResponse().fail().message("读取图片Url失败");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }
//    @ResponseBody
//    @RequestMapping("/getImageUrl")
//    public SystemResponse getImageUrl(String type){
//        String head = "F:/reptile/";
//        String[] array = new String[]{head+"fengjing.txt",head+"meizi.txt",head+"pure.txt"};
//        String pathName = array[Integer.parseInt(type)];
//        List<String> list = new ArrayList();
//        try {
//            FileReader fileReader = new FileReader(pathName);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                // 一次读入一行数据
//                list.add(line);
//            }
//            return new SystemResponse().success().message("读取图片Url成功").data(list);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new SystemResponse().fail().message(e.getMessage());
//        }
//    }
}
