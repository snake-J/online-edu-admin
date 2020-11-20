package com.atguigu.staservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient{
    @Override
    public R countRegister(String day) {
        return R.error();
    }
}
