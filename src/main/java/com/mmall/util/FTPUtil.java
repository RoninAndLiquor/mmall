package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIP = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, Integer port, String user, String pass) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public static boolean uploadFile(List<File> fileList,String dir) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIP,21,ftpUser,ftpPass);
        LOG.info("开始连接FPT服务器");
        boolean result = ftpUtil.uploadFile(dir, fileList);
        LOG.info("结束上传，上传结果{}",result);
        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if(connectServer(this.ip,this.port,this.user,this.pass)){
            try{
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            }catch (IOException e){
                LOG.error("*** 上传文件异常 ***"+e.getMessage());
                uploaded = false;
            }finally {
                if(fis!=null){
                    fis.close();
                    ftpClient.disconnect();
                }
            }
        }
        return uploaded;

    }

    private boolean connectServer(String ip,int port,String user,String pass){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess =  ftpClient.login(user,pass);
        } catch (IOException e) {
            LOG.error("*** 连接FTP服务器异常 ***"+e.getMessage());
        }
        return isSuccess;
    }

    private String ip;
    private Integer port;
    private String user;
    private String pass;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
