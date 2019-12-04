package com.example.myproject.controller;

import com.example.myproject.common.annotation.FileMonitor;
import com.example.myproject.common.utils.PropertiesUtil;
import com.example.myproject.entity.SystemResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    @RequestMapping("/uploadMng")
    public String uploadMng(){
        return "upload/uploadMng";
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    @FileMonitor
    @ResponseBody
    @PostMapping("/uploadFile")
    public SystemResponse UpLoadFile(HttpServletRequest request, @RequestParam("file") List<MultipartFile> multipartFile){
        String path = PropertiesUtil.getProperty("system.listener.path");
        File folder = new File(path);
        if (!folder.isDirectory())  folder.mkdirs();

        for(int i=0;i<multipartFile.size();i++) {
            String oldName = multipartFile.get(i).getOriginalFilename();
            try {
                multipartFile.get(i).transferTo(new File(folder, oldName));
                return new SystemResponse().success().data(path+oldName);
            } catch (Exception e) {
                e.printStackTrace();
                return new SystemResponse().fail().message(e.getMessage());
            }
        }
        return new SystemResponse().success().data("");
    }
}
