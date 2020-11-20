package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");
        //查询所有课程号下的小节
        List<EduVideo> eduVideoList = baseMapper.selectList(queryWrapper);
        //存放所有的视频id
        List<String> videoIdList=new ArrayList<>();

        //遍历小节列表
        for (int i = 0; i < eduVideoList.size(); i++) {
            //获取视频源id
            String videoSourceId = eduVideoList.get(i).getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                videoIdList.add(videoSourceId);
            }
        }
        //判断小节下是否有视频
        if (videoIdList.size()>0){
            vodClient.removeVideoList(videoIdList);
        }

        QueryWrapper<EduVideo> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("course_id",courseId);
        baseMapper.delete(queryWrapper2);
    }

    @Override
    public void removeVideoById(String videoInfoId) {
        //根据小节id查询视频源id，判断后再进行删除
        EduVideo eduVideo = baseMapper.selectById(videoInfoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            R result = vodClient.removeVideo(videoSourceId);
            if(result.getCode()==20001){
                throw new GuliException(20001,"删除视频失败,服务未找到");
            }
        }
        baseMapper.deleteById(videoInfoId);
    }
}
