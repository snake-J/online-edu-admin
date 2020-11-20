package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.vo.CourseInfo;
import com.atguigu.commonutils.vo.UcenterMember;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private EduClient eduClient;


    @Override
    public String createOrders(String courseId, String memberId) {
        //通过远程调用，根据会员id获取用户信息
        UcenterMember memberInfo = ucenterClient.getMemberInfoById(memberId);
        //通过远程调用，根据课程id获取信息
        CourseInfo courseInfo = eduClient.getCourseInfoById(courseId);
        //创建订单
        Order order=new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        //课程信息填入
        order.setCourseId(courseInfo.getId());
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        //用户信息填入
        order.setMemberId(memberInfo.getId());
        order.setNickname(memberInfo.getNickname());
        order.setMobile(memberInfo.getMobile());

        order.setStatus(0);//订单状态
        order.setPayType(1);//支付类型，默认1，为微信支付

        //生成订单
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
