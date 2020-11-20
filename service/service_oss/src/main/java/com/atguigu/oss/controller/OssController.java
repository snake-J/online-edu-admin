package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description="阿里云文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;
    //上传头像的方法
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public R uploadOssFile(
            @ApiParam(name = "file", value = "文件", required = true)
            MultipartFile file
    ){
        //获取上传的文件   MultipartFile
        //返回上传到oss的路径
        String url=ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }

    @ApiOperation(value = "文件删除")
    @DeleteMapping("removeImg")
    public R removeImg(
            @ApiParam(name = "imgName",value = "文件名",required = true)
            @RequestParam("imgName") String imgName
    ){
        ossService.removeImg(imgName);
        return R.ok();
    }
}
