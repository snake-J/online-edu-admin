package com.atguigu.eduorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UcenterClient {
    @GetMapping("/educenter/member/getInfo/{id}")
    public com.atguigu.commonutils.vo.UcenterMember getMemberInfoById(@PathVariable("id") String id);
}
