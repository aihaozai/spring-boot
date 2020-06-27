package com.example.myproject.common.FTP;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import java.io.*;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-13 14:02
 **/
@Slf4j
public class FTPSUtil {
    //ftp服务器地址
    private String hostname;
    //ftp服务器端口号默认为21
    private Integer port;
    //ftp登录账号
    private String username;
    //ftp登录密码
    private String password;

    private SSLFTPSClient ftpsClient;

    public FTPSUtil(String hostname, Integer port, String username,String password){
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.initftpsClient();
    }

    /**
     * 初始化ftp服务器
     */
    private void initftpsClient() {
        ftpsClient = new SSLFTPSClient();
        ftpsClient.setControlEncoding("utf-8");
        try {
            log.info("正在连接FTP服务器...[IP: {},PORT: {}]",this.hostname,this.port);
            ftpsClient.connect(hostname, port); //连接ftp服务器
            ftpsClient.login(username, password); //登录ftp服务器
            int replyCode = ftpsClient.getReplyCode(); //是否成功登录服务器
            if(!FTPReply.isPositiveCompletion(replyCode)){
                log.error("连接FTP服务器失败...[IP: {},PORT: {}]",this.hostname,this.port);
            }else {
                ftpsClient.setFileType(ftpsClient.BINARY_FILE_TYPE);
                ftpsClient.execPBSZ(0);
                ftpsClient.execPROT("P");
                ftpsClient.setControlEncoding("UTF-8");
                ftpsClient.enterLocalPassiveMode();
                log.info("连接FTP服务器成功...[IP: {},PORT: {}]",this.hostname,this.port);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean uploadFile( String pathname, String fileName,InputStream inputStream){
        boolean flag = false;
        try{
            log.info("开始上传文件");
            createDirecroty(pathname);
            ftpsClient.makeDirectory(pathname);
            ftpsClient.changeWorkingDirectory(pathname);
            flag = ftpsClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpsClient.logout();
            log.info("上传文件成功");
        }catch (Exception e) {
            log.error("上传文件失败");
            e.printStackTrace();
        }finally{
            if(ftpsClient.isConnected()){
                try{
                    ftpsClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public boolean uploadFileByFilePath( String pathname, String fileName,String originFileName) throws FileNotFoundException {
        return uploadFile(pathname,fileName,new FileInputStream(new File(originFileName)));
    }
    
    public boolean uploadFileByFile( String pathname, String fileName,File file) throws FileNotFoundException {
        return uploadFile(pathname,fileName,new FileInputStream(file));
    }

    //改变目录路径
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpsClient.changeWorkingDirectory(directory);
            if (flag) {
                log.info("进入文件夹" + directory + " 成功！");

            } else {
                log.error("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public boolean createDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote;
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(directory)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            StringBuilder paths = new StringBuilder();
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        log.error("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths.append("/").append(subDirectory);
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    //判断ftp服务器文件是否存在
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpsClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }
    //创建目录
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpsClient.makeDirectory(dir);
            if (flag) {
                log.info("创建文件夹" + dir + " 成功！");

            } else {
                log.error("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /** * 下载文件 *
     * @param pathname FTP服务器文件目录 *
     * @param filename 文件名称 *
     * @param localpath 下载后的文件路径 *
     * @return */
    public  boolean downloadFile(String pathname, String filename, String localpath){
        boolean flag = false;
        OutputStream os=null;
        try {
            log.info("开始下载文件");
            //切换FTP目录
            ftpsClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpsClient.listFiles();
            for(FTPFile file : ftpFiles){
                if(filename.equalsIgnoreCase(file.getName())){
                    File localFile = new File(localpath + "/" + file.getName());
                    os = new FileOutputStream(localFile);
                    flag = ftpsClient.retrieveFile(file.getName(), os);
                    os.close();
                    log.info("下载文件成功");
                }
            }
            ftpsClient.logout();
        } catch (Exception e) {
            log.error("下载文件失败");
            e.printStackTrace();
        } finally{
            if(ftpsClient.isConnected()){
                try{
                    ftpsClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /** * 删除文件 *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return */
    public boolean deleteFile(String pathname, String filename){
        boolean flag = false;
        try {
            log.info("开始删除文件");
            //切换FTP目录
            ftpsClient.changeWorkingDirectory(pathname);
            int num = ftpsClient.dele(filename);
            ftpsClient.logout();
            if(num==250){
                log.info("删除文件成功");
                flag = true;
            }
        } catch (Exception e) {
            log.error("删除文件失败");
            e.printStackTrace();
        } finally {
            if(ftpsClient.isConnected()){
                try{
                    ftpsClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //FtpUtil ftpUtil = new FtpUtil("127.0.0.1",21,"admin","admin");
       // ftpUtil.uploadFileByFilePath("/ftp/","xx.txt","h://test/xx.txt");
        //boolean f = ftpUtil.downloadFile("/ftp/","xx.txt","f://");
        //ftpUtil.deleteFile("/ftp/","xx.txt");
    }

}
