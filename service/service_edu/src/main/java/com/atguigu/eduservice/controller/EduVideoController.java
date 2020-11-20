package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-24
 */
@Api(description="小节管理")
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    //添加小节
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    @ApiOperation(value = "删除小节")
    @DeleteMapping("{videoInfoId}")
    public R deleteVideo(@PathVariable String videoInfoId){
        eduVideoService.removeVideoById(videoInfoId);
        return R.ok();
    }

    //修改小节
    //1.根据id查询小节
    @ApiOperation(value = "根据id查询小节")
    @GetMapping("getVideoInfo/{videoInfoId}")
    public R getVideoInfo(@PathVariable String videoInfoId){
        EduVideo eduVideo = eduVideoService.getById(videoInfoId);
        return R.ok().data("eduVideo",eduVideo);
    }
    //2.更新小节
    @ApiOperation(value = "更新小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }


}

