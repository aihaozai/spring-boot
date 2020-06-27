package com.example.myproject.common.utils.imageUtil;




import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-12-05 17:45
 **/

@Slf4j
public class ImageEncodeUtil {

    //1-压缩图片
    public static InputStream  compressFile(InputStream input,String ext) throws IOException {
        //1-压缩图片
        BufferedImage bufImg = ImageIO.read(input);// 把图片读入到内存中
        bufImg = Thumbnails.of(bufImg).width(100).keepAspectRatio(true).outputQuality(0.2f).asBufferedImage();//压缩:宽度100px,长度自适应;质量压缩到0.1
        ByteArrayOutputStream bos = new ByteArrayOutputStream();// 存储图片文件byte数组
        ImageIO.write(bufImg, ext, bos); // 图片写入到 ImageOutputStream
        input = new ByteArrayInputStream(bos.toByteArray());
        int available = input.available();
        //2-如果大小超过50KB，继续压缩
        if(available > 50000){
            compressFile(input,ext);
        }
        return input;
    }
    //2-InputStream转化为base64
    public static String getBase64FromInputStream(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(Base64.encodeBase64(data));
    }

    //3-Base64解码并生成图片
    public static boolean GenerateImage(String base64str,String savepath) { //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            // System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据（这一步很重要）
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
