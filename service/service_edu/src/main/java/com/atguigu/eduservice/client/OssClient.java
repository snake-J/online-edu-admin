package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-oss")
@Component
public interface OssClient {
    @DeleteMapping("/eduoss/fileoss/removeImg")
    public R removeImg(@RequestParam("imgName")String imgName);
}
