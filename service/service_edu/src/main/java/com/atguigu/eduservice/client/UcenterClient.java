package com.atguigu.eduservice.client;

import com.atguigu.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    @GetMapping("/educenter/member/getInfo/{id}")
    public UcenterMember getMemberInfoById(@PathVariable("id") String id);
}
