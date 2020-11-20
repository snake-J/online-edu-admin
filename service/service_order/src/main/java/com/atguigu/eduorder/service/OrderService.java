package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-08
 */
public interface OrderService extends IService<Order> {
    //一、生成订单的方法
    String createOrders(String courseId, String memberId);
}
