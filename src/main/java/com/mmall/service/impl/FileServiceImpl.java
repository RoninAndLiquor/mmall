package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path,String dir){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.indexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        LOG.info("*** 开始上传文件，上传的文件名:{},上传的路径:{},新文件名:{} ***",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //文件上传至upload文件夹
            file.transferTo(targetFile);
            //文件上传至FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile),dir);
            //删除upload中的图片
            targetFile.delete();
        }catch (IOException e) {
            LOG.error("*** 上传文件异常 ***"+e.getMessage());
            return null;
        }
        return dir+"/"+targetFile.getName();
    }

}
