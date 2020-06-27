package com.example.myproject.common.utils.imageUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-27 17:38
 **/
public class DocumentUtil {
    public static Document getDocument(String url) {
        try {
            // 5000是设置连接超时时间，单位ms
            Connection connection =  Jsoup.connect(url);
            connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.header("Accept-Encoding", "gzip, deflate, sdch");
            connection.header("Accept-Language", "zh-CN,zh;q=0.8");
            connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            connection.header("Cache-Control","no-cache");
            connection.header("Pragma","no-cache");
            connection.header("Content-Type","application/x-www-form-urlencoded");
            connection.header("Connection","keep-alive");
            return connection.timeout(5000).get();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        return null;
    }
}
