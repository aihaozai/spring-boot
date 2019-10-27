package com.example.myproject.controller;

import com.example.myproject.common.imageUtil.MeiZiUtil;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-26 23:58
 **/
public class Test {
    public static void main(String[] args) throws Exception{
        //http://www.meizitu.net.cn/
        //http://pic.netbian.com/4kfengjing/
        MeiZiUtil.getClassification("http://www.meizitu.net.cn");
      //  MeiZiTuUtil.getClassification("https://www.mzitu.com/xinggan");
       // ImageUtil.getClassification("http://pic.netbian.com/4kfengjing");
    }

}
