package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantPropertiesUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //2.获取扫描人的信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            //1.获取code值
            //2.拿着code请求固定地址，得到返回的两个值，一个access_token和openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtils.WX_OPEN_APP_ID,
                    ConstantPropertiesUtils.WX_OPEN_APP_SECRET,
                    code);
            //通过httpclient请求目标地址，返回字符串
            String result = null;

            result = HttpClientUtils.get(accessTokenUrl);
//            System.out.println("result："+result);

            //解析json字符串
            Gson gson = new Gson();
            HashMap hashMap = gson.fromJson(result, HashMap.class);
            String accessToken = (String) hashMap.get("access_token");
            String openid = (String) hashMap.get("openid");

            //查询是否有该openid
            UcenterMember member=ucenterMemberService.getMemberByOpenid(openid);
            if(member==null){
                //拿着accessToken和openid再去请求一个微信的固定地址，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl,
                        accessToken,
                        openid
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap user = gson.fromJson(userInfo, HashMap.class);
                System.out.println(user);
                String nickname = (String) user.get("nickname");
                String headimgurl = (String) user.get("headimgurl");
                member = new UcenterMember();
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openid);
                ucenterMemberService.save(member);
            }
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token="+token;
        } catch (Exception e) {
            throw new GuliException(20001,"扫码登陆失败！");
        }


    }


    //1.生成二维码
    @GetMapping("login")
    public String getWxCode(){
        //微信开放平台授权，%s相当于？代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirecturl进行URLEncoder编码
        String redirectUrl=ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl= URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001,e.getMessage());
        }
        //设置%s的值
        String url = String.format(
                baseUrl,
                ConstantPropertiesUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        //重定向到请求微信地址
        return "redirect:"+url;
    }
}
