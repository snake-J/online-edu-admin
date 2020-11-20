package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseInfo;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.frontVo.CourseFrontInfoVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseListVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.peer.PanelPeer;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Api(description="课程信息管理")
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;


    //添加课程基本信息的方法
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id=eduCourseService.saveCourseInfo(courseInfoVo);
        if(!StringUtils.isEmpty(id)){
            return R.ok().data("courseId",id);
        }else{
            return R.error().message("保存失败");
        }

    }

    //根据课程id查询课程基本信息
    @ApiOperation(value = "根据ID查询课程信息")
    @GetMapping("getCourseInfo/{courseId}")
    public R getById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId
    ){
        CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id=eduCourseService.updateCourseInfo(courseInfoVo);
        if(!StringUtils.isEmpty(id)){
            return R.ok().data("courseId",id);
        }else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation(value = "课程发布信息")
    @GetMapping("coursePublishInfo/{id}")
    public R selectCoursePublishVoById(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.selectCoursePublishVoById(id);
        return R.ok().data("item",coursePublishVo);
    }

    @ApiOperation(value = "课程最终发布")
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse=new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        boolean b = eduCourseService.updateById(eduCourse);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value ="课程列表")
    @PostMapping("getCourseList/{current}/{limit}")
    public R getCourseList(
            @PathVariable Long current,
            @PathVariable Long limit,
            @RequestBody CourseListVo courseListVo
            ){
        Page<EduCourse> pageCourse=new Page<>(current,limit);
        eduCourseService.pageCourseQuery(pageCourse,courseListVo);
        List<EduCourse> rows = pageCourse.getRecords();
        long total = pageCourse.getTotal();
        return R.ok().data("total",total).data("rows",rows);
    }

    @ApiOperation(value = "删除课程")
    @DeleteMapping("{courseId}")
    public R removeCourse(@PathVariable String courseId){
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }


    //根据课程id返回课程信息对象
    @GetMapping("getDto/{courseId}")
    public CourseInfo getCourseInfoById(@PathVariable String courseId){
        CourseFrontInfoVo baseCourseInfo = eduCourseService.getBaseCourseInfo(courseId);
        CourseInfo courseInfo=new CourseInfo();
        BeanUtils.copyProperties(baseCourseInfo,courseInfo);
        return courseInfo;


    }

}

