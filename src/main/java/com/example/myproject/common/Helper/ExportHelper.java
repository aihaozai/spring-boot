package com.example.myproject.common.Helper;

import net.sf.jxls.transformer.XLSTransformer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-29 10:08
 **/
@Component
public class ExportHelper {
    public void expRecords(HttpServletRequest request, HttpServletResponse response, List resultList, String srcFileName, String FileName) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //获取年、月、日
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);

            //导出文件名称
            String fileName = FileName;

            fileName += "-导出时间：" + year + "年" + month + "月" + day + "日";

            //目标文件路径
            Resource resource = new ClassPathResource("export/xls/"+srcFileName);
            //模版文件路径
            String srcFilePath = resource.getFile().getPath();

            String destFilePath = this.getClass().getClassLoader().getResource("").getPath()+FileName;

            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("list", resultList);
            mapResult.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            mapResult.put("title", fileName);

            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(srcFilePath, mapResult, destFilePath);
            if ((new File(destFilePath)).exists()) {//判断目标文件是否存在，存在提供下载
                response.reset();
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes
                        ("utf-8"), "ISO8859-1") + ".xls");
                File f = new File(destFilePath);
                bis = new BufferedInputStream(new FileInputStream(f));
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];

                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
