package com.atguigu.vod.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAlyVideo(MultipartFile file);

    void removeVideo(String id);

    void removeVideoList(List videoIdList);
}
