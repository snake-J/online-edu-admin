package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontVo.CourseFrontInfoVo;
import com.atguigu.eduservice.entity.frontVo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseListVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    String updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo selectCoursePublishVoById(String id);

    void pageCourseQuery(Page<EduCourse> pageCourse, CourseListVo courseListVo);

    void removeCourse(String courseId);

    //前台查询8个课程
    List<EduCourse> getCourseFront();


    //根据教师id查询课程信息
    List<EduCourse> getCourseListById(String id);

    Map<String, Object> getCourseList(Page<EduCourse> p, CourseQueryVo courseQueryVo);

    CourseFrontInfoVo getBaseCourseInfo(String courseId);

    void updatePageViewCount(String id);
}
