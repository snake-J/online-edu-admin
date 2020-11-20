package com.atguigu.eduservice.client;


import org.springframework.stereotype.Component;

@Component
public class OrderFile implements OrderClient{

    @Override
    public boolean isBuyCourse(String memberid, String id) {
        return false;
    }
}
