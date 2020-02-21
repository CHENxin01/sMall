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

/**
 * Created by chen
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //返回上传文件名
    public String upload(MultipartFile file,String path) {
        //原文件名
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //上传唯一id，防止上传相同的图片名覆盖原图片
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);


        File fileDir = new File(path);
        if (!fileDir.exists()) {
            //赋予可写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        //创建目标文件
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件已经上传成功。（在upload文件了）

            //将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经把文件上传到ftp服务器上了

            // 上传完后，删除upload下面的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常",e );
            return null;
        }

        return targetFile.getName();

    }
}
