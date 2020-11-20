package com.atguigu.educms.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OssFileDegradeFeignClient implements OssClient{

    @Override
    public R removeImg(String imgName) {
        return R.error();
    }
}
