package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-22
 */
@Api(description="课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
//@CrossOrigin
public class  EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;


    //添加课程分类
    //获取上传到的文件，再读取
    @ApiOperation(value = "Excel批量导入")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }

    //课程分类列表
    @ApiOperation(value = "课程分类列表")
    @GetMapping("findAll")
    public R findAll(){
        List<OneSubject> subjects=eduSubjectService.findAll();
        return R.ok().data("items",subjects);
    }
}

