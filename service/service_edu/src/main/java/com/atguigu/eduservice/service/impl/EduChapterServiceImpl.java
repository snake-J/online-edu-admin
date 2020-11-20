package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //获取章节
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_Id",courseId);
        queryWrapper.orderByAsc("sort", "id");
        List<EduChapter> eduChapters = baseMapper.selectList(queryWrapper);
        //获取小节
        QueryWrapper<EduVideo> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("course_Id",courseId);
        queryWrapper1.orderByAsc("sort", "id");
        List<EduVideo> eduVideos = eduVideoService.list(queryWrapper1);


        //创建最终list集合
        List<ChapterVo> finalList=new ArrayList<>();


        for (int i = 0; i < eduChapters.size(); i++) {
            //封装章节
            EduChapter eduChapter = eduChapters.get(i);
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);
            //封装小节

            List<VideoVo> videoList=new ArrayList<>();
            for (int j = 0; j < eduVideos.size(); j++) {
                EduVideo eduVideo = eduVideos.get(j);

                //创建小节列表，最后添加到最终集合的children
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo=new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoList);

        }
        return finalList;

    }
    //删除章节
    @Override
    public boolean delChapter(String chapterId) {
        //根据章节id查询小节表
        QueryWrapper<EduVideo> qw=new QueryWrapper<>();
        qw.eq("chapter_id",chapterId);
        //不需要小节数据，只需要知道章节里是否有小节。可使用count方法
        int count = eduVideoService.count(qw);
        if(count>0){//查询出有小节，不进行删除
            throw new GuliException(20001,"不能删除");
        }else { //不能查出，可删除
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
