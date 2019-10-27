package com.example.myproject.common.utils;

import com.example.myproject.entity.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-05 18:44
 **/
@Slf4j
@Component
public class MailUtil {
    @Value("${spring.mail.from}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     *@author Jen
     *@Point  发送简单邮件
     *@Param  mail
     *@return boolean
     */
    public boolean sendMail(Mail mail){
        String to = mail.getTo();// 目标邮箱
        String title = mail.getTitle();// 邮件标题
        String content = mail.getContent();// 邮件正文
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        try {
            javaMailSender.send(message);
            log.info("邮件发送成功");
            return true;
        } catch (MailException e) {
            log.error("邮件发送失败, to: {}, title: {}", to, title, e);
            return false;
        }
    }
}
