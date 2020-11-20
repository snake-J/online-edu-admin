package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.OssClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.frontVo.CourseFrontInfoVo;
import com.atguigu.eduservice.entity.frontVo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseListVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private OssClient ossClient;


    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //添加课程基本信息
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.insert(eduCourse);
        if(i==0){
            throw new GuliException(20001,"添加课程失败");
        }
        String cid=eduCourse.getId();

        //添加课程描述信息
        EduCourseDescription description=new EduCourseDescription();
        description.setId(cid);
        description.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.save(description);

        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);

        //2.查询课程描述表
        EduCourseDescription description = eduCourseDescriptionService.getById(courseId);

        //3.创建CourseInfo对象,封装数据
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        //4.封装课程表数据
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //5.封装描述表数据
        courseInfoVo.setDescription(description.getDescription());

        //6.返回
        return courseInfoVo;

    }

    @Override
    public String updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.提取courseInfoVo中有关课程信息的数据
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        //2.更新课程表信息的数据
        int i = baseMapper.updateById(eduCourse);
        if(i==0){
            throw new GuliException(20001,"添加课程失败");
        }
        String cid=eduCourse.getId();

        //3.更新描述信息
        EduCourseDescription eduCourseDescription=new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
        return cid;
    }

    @Override
    public CoursePublishVo selectCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    //分页条件查询课程列表
    @Override
    public void pageCourseQuery(Page<EduCourse> pageCourse, CourseListVo courseListVo) {
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if(courseListVo==null){
            baseMapper.selectPage(pageCourse,queryWrapper);
        }
        String title = courseListVo.getTitle();
        String status = courseListVo.getStatus();
        String subjectParentId = courseListVo.getSubjectParentId();
        String subjectId = courseListVo.getSubjectId();

        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        baseMapper.selectPage(pageCourse,queryWrapper);
    }

    @Override
    public void removeCourse(String courseId) {
        //TODO 删除视频

        //根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //根据课程id删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //根据课程id删除描述
        eduCourseDescriptionService.removeById(courseId);
        //删除封面
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",courseId);
        String cover = baseMapper.selectOne(queryWrapper).getCover();
        String imgName= org.apache.commons.lang.StringUtils.substringAfter(cover,"com/");
        ossClient.removeImg(imgName);
        //根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if(result==0){
            throw new GuliException(20001,"删除失败");
        }

    }

    @Cacheable(value = "course",key = "'selectCourseList'")
    @Override
    public List<EduCourse> getCourseFront() {
        QueryWrapper<EduCourse> q1=new QueryWrapper<>();
        q1.orderByDesc("id");
        q1.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(q1);
        return eduCourses;
    }

    @Override
    public List<EduCourse> getCourseListById(String id) {
        QueryWrapper<EduCourse> qw=new QueryWrapper<>();
        qw.eq("teacher_id",id);
        qw.orderByDesc("gmt_modified");
        List<EduCourse> eduCourses = baseMapper.selectList(qw);
        return eduCourses;
    }

    @Override
    public Map<String, Object> getCourseList(Page<EduCourse> p, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseQueryVo.getSubjectId())){
            queryWrapper.eq("subject_id",courseQueryVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseQueryVo.getPriceSort())){
            queryWrapper.orderByDesc("price");
        }
        baseMapper.selectPage(p,queryWrapper);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("total",p.getTotal());
        hashMap.put("items",p.getRecords());
        hashMap.put("pages",p.getPages());
        hashMap.put("current",p.getCurrent());
        hashMap.put("size",p.getSize());
        hashMap.put("hasNext",p.hasNext());
        hashMap.put("hasPrevious",p.hasPrevious());

        return hashMap;
    }

    @Override
    public CourseFrontInfoVo getBaseCourseInfo(String courseId) {
        this.updatePageViewCount(courseId);
        return baseMapper.selectInfoById(courseId);
    }

    @Override
    public void updatePageViewCount(String id) {
        EduCourse eduCourse = baseMapper.selectById(id);
        eduCourse.setViewCount(eduCourse.getViewCount()+1);
        baseMapper.updateById(eduCourse);
    }

}
