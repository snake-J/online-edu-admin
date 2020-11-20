package com.atguigu.msmservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件
    @Value("${aliyun.msm.keyid}")
    private String keyId;
    @Value("${aliyun.msm.keysecret}")
    private String keySecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override
    //所有的属性被初始化后调用
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID=keyId;
        ACCESS_KEY_SECRET=keySecret;
    }
}
