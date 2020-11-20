package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontVo.CourseFrontInfoVo;
import com.atguigu.eduservice.entity.frontVo.CourseQueryVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description="课程前台显示")
@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrderClient orderClient;

    @PostMapping("getCourseFrontList/{page}/{limit}")
    public R getCourseList(@PathVariable long page, @PathVariable long limit,
                           @RequestBody(required = false) CourseQueryVo courseQueryVo
                           ){

        Page<EduCourse> p=new Page<>(page,limit);
        Map<String,Object> map=eduCourseService.getCourseList(p,courseQueryVo);
        return R.ok().data(map);
    }

    @GetMapping("getCourseFrontInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId,HttpServletRequest request){
        //1.根据课程id，编写sql语句查询课程信息
        CourseFrontInfoVo courseInfoVo=eduCourseService.getBaseCourseInfo(courseId);
        //2.根据课程id查询章节小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);
        //3.远程调用，判断课程是否被购买
        boolean flag = orderClient.isBuyCourse(JwtUtils.getMemberIdByJwtToken(request), courseId);
        return R.ok().data("courseInfo",courseInfoVo).data("chapterVideoList",chapterVideoList).data("isBuy",flag);
    }
}
