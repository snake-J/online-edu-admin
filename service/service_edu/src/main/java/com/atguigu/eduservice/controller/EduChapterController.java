package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Api(description="课程大纲及章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;

    //返回课程大纲列表
    @ApiOperation(value = "课程大纲列表")
    @GetMapping("getChapterVideo/{courseId}")
    public R addChapter(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){
        List<ChapterVo> list=eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo",list);
    }

    //添加章节
    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    //修改章节
    //1.根据id查询章节
    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return R.ok().data("eduChapter",eduChapter);
    }
    //2.更新章节
    @ApiOperation(value = "更新章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节
    //如果章节下有小节，则不删除，无小节再进行删除。
    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag=eduChapterService.delChapter(chapterId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }


}

