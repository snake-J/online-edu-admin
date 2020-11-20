package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//服务调用注解,指定被调用端服务名称
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
//添加组件注解，交给spring管理
@Component
public interface VodClient {
    //service-vod下的要调用的方法
    //注：路径写全！and @PathVariable注解要指定名称
    @DeleteMapping("/eduvod/video/{id}")
    public R removeVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/video/deleteBatch")
    public R removeVideoList(@RequestParam("videoIdList") List<String> videoIdList);

}
