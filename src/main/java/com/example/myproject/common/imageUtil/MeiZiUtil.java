package com.example.myproject.common.imageUtil;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.myproject.common.imageUtil.DocumentUtil.getDocument;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-27 18:09
 **/
public class MeiZiUtil {
    public static String path = "F:/meizitu/"; //存放文件地址

    public static long restTime = 3000; //休眠时间 （豪秒）不能太快

    public static void getClassification(String url)throws IOException {

        Document doc = getDocument(url);
        // 获取总页数html
        Elements PageCountHtml = doc.select("[class=pagination custom-pagination]").select("a");
        //当前大分类总页数
        int PageCount = Integer.parseInt(PageCountHtml.get(PageCountHtml.size()-2).text());
        System.out.println("分类页数"+PageCount+"页");
        File file = new File("F:/meizi.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i = 1; i <PageCount; i++) {
            getEachPage(url+"/page/"+i,bw);
        }
        bw.flush();
        System.out.println("图集地址抓取完毕");
    }

    private static void getEachPage(String url,BufferedWriter bw)throws IOException{

        Document doc = getDocument(url);
        if(doc!=null) {
            Elements postList = doc.select("[class=thumb]");
            for (int i = 0; i < postList.size(); i++) {
                Elements liCount = postList.select("img");
                Elements a =  postList.select("a");
                String title = a.get(i).text();
                String imgUrl =liCount.get(i).attr("src");
                String Suffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                String name = title + "_" + i + Suffix; //文件名
                String savePath = path + "meizi"; //保存文件地址
                try {
                    bw.write(imgUrl + "\r\n");
                    System.out.println("抓取第" + i + "张图片("+title+")");
                    System.out.println("图片地址:" + imgUrl);
                    DownloadImage.download(imgUrl, name, savePath,restTime);
                } catch (Exception e) {
                    System.out.println("读取失败或写入失败");
                    e.printStackTrace();
                }
            }
        }
    }
}
