package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import static com.atguigu.vodtest.InitObject.initVodClient;

public class TestVod2 {
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId("f842d4c82fa0439b89502613c5dad78e");
        return client.getAcsResponse(request);
    }
    public static void getPlayAuth(){
        DefaultAcsClient client= null;
        try {
            client=initVodClient("keyid","keysecret");
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            response=getVideoPlayAuth(client);
            System.out.println("palyAuth:"+response.getPlayAuth());
            System.out.println("title:"+response.getVideoMeta().getTitle());
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        //上传视频
            String accessKeyId="LTAI4GL2FzQb3uP6rXX6QYSJ";
            String accessKeySecret="myB9XvBtvcu5s2nU0cY1It7Py9cHio";
            String title="6 - What If I Want to Move Faster - upload by sdk";
            String fileName="C:\\Users\\13604\\Desktop\\在线教育项目实战资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4";
            UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
            /* 可指定分片上传时每个分片的大小，默认为2M字节 */
            request.setPartSize(2 * 1024 * 1024L);
            /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
            request.setTaskNum(1);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadVideoResponse response = uploader.uploadVideo(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
    }
}
