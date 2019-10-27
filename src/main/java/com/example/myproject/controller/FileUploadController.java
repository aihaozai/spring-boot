package com.example.myproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class FileUploadController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    @PostMapping("/upload")
    public String UpLoadFile(HttpServletRequest request, List<MultipartFile> multipartFile){
        String realpath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        String format = sdf.format(new Date());
        File folder = new File(realpath+format);
        if (!folder.isDirectory()){
            folder.mkdirs();
        }
        for(int i=0;i<multipartFile.size();i++) {
            String oldName = multipartFile.get(i).getOriginalFilename();
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
            try {
                multipartFile.get(i).transferTo(new File(folder, newName));
                String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                        "/uploadFile/" + format + newName;
                //return filePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "上传success";
    }
}
