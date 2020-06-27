package com.example.myproject.common.utils.imageUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.myproject.common.utils.imageUtil.DocumentUtil.getDocument;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-27 10:15
 **/
public class JDUtil {

    public static String path = "F:/reptile/jd/"; //存放文件地址
    public static long restTime = 3000; //休眠时间 （豪秒）不能太快

    public static void main(String[] args) throws IOException {
        String url ="https://item.jd.com/31464764560.html";
        String head = "https://img30.360buyimg.com/sku/";
        getClassification(url,head);
    }
    public static List<String> getClassification(String url, String head)throws IOException{
        Document doc = getDocument(url);
        if(doc!=null) {
            Elements e = doc.getElementsByTag("script").eq(0);
            String result = "";
            List<String> list = new ArrayList<>();
            for (Element element : e) {
                /*取得JS变量数组*/
                String[] data = element.data().replaceAll(" ", "").replaceAll("\r|\n", "")
                        .split("imageList:\\[")[1].replaceAll("\"", "").split("],cat");
                result = data[0];
                break;
            }
            for (String u :result.split(",")){
                list.add(head+u);
            }
            return list;
        }
        return new ArrayList<>();
    }
}
