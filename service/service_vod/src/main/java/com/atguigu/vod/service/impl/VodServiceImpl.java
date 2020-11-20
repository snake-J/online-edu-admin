package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantPropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.atguigu.vod.utils.InitObject.initVodClient;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadAlyVideo(MultipartFile file) {

            try {

                String fileName=file.getOriginalFilename();
                String title=fileName.substring(0,fileName.lastIndexOf("."));
                InputStream inputStream= file.getInputStream();

                UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

                UploadVideoImpl uploader = new UploadVideoImpl();
                UploadStreamResponse response = uploader.uploadStream(request);

                String videoId=null;
                if (response.isSuccess()) {
                    videoId=response.getVideoId();
                } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                    videoId=response.getVideoId();
                }
                return videoId;
            }catch (Exception e){
                return null;
            }

    }

    @Override
    public void removeVideo(String id) {
        try {
            DefaultAcsClient client = initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request=new DeleteVideoRequest();
            request.setVideoIds(id);
            DeleteVideoResponse response=client.getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            throw new GuliException(20001,"视频删除失败");
        }
    }
    //删除多个视频
    @Override
    public void removeVideoList(List videoIdList) {
        try {
            DefaultAcsClient client = initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request=new DeleteVideoRequest();
            String ids = StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(ids);
            DeleteVideoResponse response=client.getAcsResponse(request);
            System.out.println("RequestId = " + response.getRequestId() + "\n");
        } catch (ClientException e) {
            throw new GuliException(20001,"视频删除失败");
        }
    }
    //测试StringUtils的join方法
   /* public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("111");
        list.add("222");
        //111,222
        String str = StringUtils.join(list.toArray(), ",");
        System.out.println(str);

    }*/
}
