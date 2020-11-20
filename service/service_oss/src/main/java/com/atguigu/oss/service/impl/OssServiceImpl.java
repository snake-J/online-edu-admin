package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //通过工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。

        String url=null;
        try {
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //在文件名称里面添加随机唯一值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName= uuid + fileName;
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName=datePath+"/"+fileName;
            ossClient.putObject(bucketName, fileName, inputStream);
            //把上传厚的路径返回
            //https://edu-teacherimg.oss-cn-beijing.aliyuncs.com/01.jpg
            url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            ossClient.shutdown();
        }
    }

    public void removeImg(String imgName){
        //通过工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName=ConstantPropertiesUtils.BUCKET_NAME;
        OSS client=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        client.deleteObject(bucketName,imgName);
        client.shutdown();
    }

    /*public static void main(String[] args) {
        String str="https://edu-teacherimg.oss-cn-beijing.aliyuncs.com/2020/10/09/1f3305841c174daa837c251a428c930902.jpg";
        String str1 = StringUtils.substringAfter(str, "aliyuncs");
        System.out.println(str1);
    }*/
}


