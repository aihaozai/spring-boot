package com.example.myproject.common.shiro;

import lombok.Getter;
import lombok.Setter;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-09-17 11:44
 **/
@Setter
@Getter
public class ShiroProperties {
    private String loginUrl;
    private String successUrl;
    private String anonUrl;
    private String logoutUrl;
    private long sessionTimeout;
    private int cookieTimeout;

}
