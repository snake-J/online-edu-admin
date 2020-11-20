package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-13
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin
public class EduTeacherController {

    //访问地址：http://localhost:8001/eduservice/teacher/findAll
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //查询讲师表所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        //调用service方法
//        return teacherService.list(null);
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据id逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacherById(@ApiParam(name = "id",value = "讲师ID",required = true) @PathVariable String id){
//        return teacherService.removeById(id);
        teacherService.removeById(id);
        return R.ok();
    }

    //分页查询
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageList(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit
    ){
        Page<EduTeacher> pageTeacher=new Page<>(current,limit);
        teacherService.page(pageTeacher,null);

        //
        List<EduTeacher> records = pageTeacher.getRecords();

        //总记录数
        long total = pageTeacher.getTotal();

        /*Map<String,Object> hm=new HashMap<>();
        hm.put("total",total);
        hm.put("rows",records);
        return R.ok().data(hm);*/

        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "按条件分页讲师列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery",value = "查询对象",required = false)
            @RequestBody(required = false) TeacherQuery teacherQuery
            ){
        Page<EduTeacher> pageTeacher =new Page<>(current,limit);
        teacherService.pageQuery(pageTeacher,teacherQuery);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(
            @ApiParam(name = "EduTeacher",value = "讲师对象",required = true)
            @RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.save(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
    //根据id查询
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacherById(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("item",teacher);
    }

    //修改讲师
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    public R updTeacher(
            @ApiParam(name = "Eduteacher",value = "讲师对象",required = true)
            @RequestBody EduTeacher eduTeacher
    ){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

