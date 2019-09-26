package com.zhl.uatcp;

import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

/**
 * @program iot-daq
 * @description:参数类
 * @author: meilong
 * @create: 2019/08/03 08:30
 */
public class UATcpParam {

    private String applicationName = "myClient";

    private String url;

    /**
     * 0:匿名;1:验证用户名、密码,2:certificate,3:IssuedToken
     */
    private Integer authentication;

    private String userName;

    private String password;

    /**
     * None;Sign;Sign&Encrypt
     */
    private MessageSecurityMode securityMode;

    /**
     * Basic128Rsa15;Basic256;Basic256Sha256
     */
    private SecurityPolicy securityPolicie;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Integer authentication) {
        this.authentication = authentication;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MessageSecurityMode getSecurityMode() {
        return securityMode;
    }

    public void setSecurityMode(MessageSecurityMode securityMode) {
        this.securityMode = securityMode;
    }

    public SecurityPolicy getSecurityPolicie() {
        return securityPolicie;
    }

    public void setSecurityPolicie(SecurityPolicy securityPolicie) {
        this.securityPolicie = securityPolicie;
    }
}
