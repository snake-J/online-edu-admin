package com.atguigu.eduorder.client;

import com.atguigu.commonutils.vo.CourseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
@Component
public interface EduClient {
    @GetMapping("/eduservice/course/getDto/{courseId}")
    public CourseInfo getCourseInfoById(@PathVariable("courseId") String courseId);
}
