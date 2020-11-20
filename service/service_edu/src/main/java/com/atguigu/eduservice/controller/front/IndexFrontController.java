package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(description="前台首页显示")
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {
    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    @GetMapping("index")
    public R index(){
        //查询前8条课程
        List<EduCourse> CourseList=eduCourseService.getCourseFront();

        //查询前4条讲师
        List<EduTeacher> TeacherList = eduTeacherService.getTeacherFront();


        return R.ok().data("courseList",CourseList).data("teacherList",TeacherList);
    }
}
