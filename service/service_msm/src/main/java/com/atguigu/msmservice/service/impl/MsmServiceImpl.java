package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        String KeyID= ConstantPropertiesUtils.ACCESS_KEY_ID;
        String KeySecret=ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        DefaultProfile profile=DefaultProfile.getProfile("default",KeyID,KeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        //设置相关固定参数
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //前面的name固定
        request.putQueryParameter("PhoneNumbers", phone);//手机号
        request.putQueryParameter("SignName", "学习小镇在线学习网站");//签名名称
        request.putQueryParameter("TemplateCode","SMS_204750614");//模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}
