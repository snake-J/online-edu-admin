package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantPropertiesUtils;
import com.atguigu.vod.utils.InitObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Api(description="阿里云视频点播微服务")
@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;


    //上传视频到阿里云
    @PostMapping("uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file){
        String videoId=vodService.uploadAlyVideo(file);
        return R.ok().data("videoId",videoId);
    }

    //删除指定视频
    @DeleteMapping("{id}")
    public R removeVideo(@PathVariable String id){
        vodService.removeVideo(id);
        return R.ok();
    }

    @DeleteMapping("deleteBatch")
    public R removeVideoList(@RequestParam("videoIdList") List videoIdList){
        vodService.removeVideoList(videoIdList);
        return R.ok();
    }

    //获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            //初始化
            DefaultAcsClient client = InitObject.initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET);

            //请求
            GetVideoPlayAuthRequest request=new GetVideoPlayAuthRequest();
            request.setVideoId(id);

            //响应
            GetVideoPlayAuthResponse response=client.getAcsResponse(request);

            //得到播放凭证
            String playAuth = response.getPlayAuth();

            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuliException(20001,"获取凭证失败！");
        }

    }
}
