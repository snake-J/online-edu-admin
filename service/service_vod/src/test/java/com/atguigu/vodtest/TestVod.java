package com.atguigu.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

import static com.atguigu.vodtest.InitObject.initVodClient;

public class TestVod {
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        //new一个request对象，并对该对象设置视频id
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("ba2fc6a3524f48a89fc41d7735b531fa");

        //客户端client传入request参数，获取到response对象
        return client.getAcsResponse(request);
    }
    public static void main(String[] args) {
        DefaultAcsClient client= null;
        try {
            //1.初始化client客户端，传入keyid和keysecret
            client=initVodClient("LTAI4GL2FzQb3uP6rXX6QYSJ","myB9XvBtvcu5s2nU0cY1It7Py9cHio");
            //2.调用getPlayInfo方法，该方法返回一个response
            GetPlayInfoResponse response=new GetPlayInfoResponse();
            response=getPlayInfo(client);

            //3.通过response的getPlayInfoList方法获取对象，遍历并通过getPlayURL方法获取视频播放地址
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.println("playURL:"+playInfo.getPlayURL());
            }
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
