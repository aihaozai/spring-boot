package com.example.myproject.common.utils.imageUtil;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static com.example.myproject.common.utils.imageUtil.DocumentUtil.getDocument;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-27 10:15
 **/
public class ImageUtil {

    private static  String httpUrl = "http://pic.netbian.com";
    public static void getClassification(String url,String fileName)throws IOException{

        Document doc = getDocument(url);
        // 获取总页数html
        Elements PageCountHtml = doc.select("[class=page]").select("a");
        //当前大分类总页数
        int PageCount = Integer.parseInt(PageCountHtml.get(PageCountHtml.size()-2).text());
        System.out.println("分类页数"+PageCount+"页");
        BufferedWriter bw = new BufferedWriter(new FileWriter(FileUtil.getFile(fileName)));
        for (int i = 1; i <= PageCount; i++) {
            System.out.println("正在抓取第"+i+"页");
            if(i==1){
                getEachPage(url+"/index.html",bw);
            }else {
                getEachPage(url+"/index_"+i+".html",bw);
            }
        }
        bw.flush();
        System.out.println("图集地址抓取完毕");
    }
    private static void getEachPage(String url,BufferedWriter bw){
        Document doc = getDocument(url);
        if(doc!=null) {
            Elements postList = doc.select("[class=clearfix]");
            Elements liCount = postList.select("li");
            for (int i = 0; i < liCount.size(); i++) {
                Elements a = liCount.get(i).select("a").select("img");
                String imgUrl = httpUrl+a.attr("src");
                String title = a.attr("alt");//标题
                try {
                    bw.write(imgUrl + "\r\n");
                    System.out.println("抓取第" + i + "张图片("+title+")");
                    System.out.println("图片地址:" + imgUrl);
                } catch (Exception e) {
                    System.out.println("读取失败或写入失败");
                    e.printStackTrace();
                }
            }
        }
    }
}
