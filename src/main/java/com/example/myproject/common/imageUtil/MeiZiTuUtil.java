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
 * @Create: 2019-10-27 10:15
 **/
public class MeiZiTuUtil {

    public static String path = "F:/reptile/meizitu/"; //存放文件地址
    public static long restTime = 3000; //休眠时间 （豪秒）不能太快

    public static void getClassification(String url,String fileName)throws IOException{

        Document doc = getDocument(url);
        // 获取总页数html
        Elements PageCountHtml = doc.select("[class=page-numbers]");
        //当前大分类总页数
        int PageCount = Integer.parseInt(PageCountHtml.get(PageCountHtml.size()-1).text());
        System.out.println("分类页数"+PageCount+"页");
        BufferedWriter bw = new BufferedWriter(new FileWriter(FileUtil.getFile(fileName)));
        for (int i = 1; i <= PageCount; i++) {
            System.out.println("正在抓取第"+i+"页");
            getEachPage(url+"/page/"+i+"/",bw);
        }
        bw.flush();
        System.out.println("图集地址抓取完毕");
    }

    private static void getEachPage(String url,BufferedWriter bw)throws IOException{

        Document doc = getDocument(url);
        if(doc!=null) {
            Elements postList = doc.select("[class=postlist]");
            Elements liCount = postList.select("li");
            for (int i = 0; i < liCount.size(); i++) {
                Elements a = liCount.get(i).select("span").select("a");
                String aurl = a.attr("href");
                getEachGroup(aurl,bw);
            }
        }
    }

    private static void getEachGroup(String url,BufferedWriter bw){
        Document doc = getDocument(url);
        if(doc!=null) {
            Elements pageNumberHtml = doc.select("[class=pagenavi]");
            Elements a = pageNumberHtml.select("a");
            int pageNumber = Integer.parseInt(a.get(a.size() - 2).select("span").text());
            for (int i = 1; i <= pageNumber; i++) {
                String groupingurl = url + "/" + i;
                Document imgdoc = getDocument(groupingurl);
                if(imgdoc!=null) {
                    Elements select = imgdoc.select("[class=main-image]");
                    Elements img = select.select("img");
                    String imgUrl = img.attr("src"); //下载图片地址
                    String Suffix = imgUrl.substring(imgUrl.lastIndexOf("."));
                    String title = img.attr("alt");//标题

                    String name = title + "_" + i + Suffix; //文件名
                    String savePath = path + title; //保存文件地址

                    try {
                        bw.write(imgUrl + "\r\n");
                        System.out.println("抓取组(" + title + ")第" + i + "张图片");
                        System.out.println("图片地址:" + imgUrl);
                    } catch (Exception e) {
                        System.out.println("读取失败或写入失败");
                        e.printStackTrace();
                    }
                }
//            try {
//                DownloadImage.download(imgUrl, name, savePath,restTime);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            }
        }
    }

}
