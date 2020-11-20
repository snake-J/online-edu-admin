package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-03
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //评论分页
    @Override
    public Map<String, Object> getCommentList(Page<EduComment> p, String courseId) {
        QueryWrapper<EduComment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_Id",courseId);
        baseMapper.selectPage(p,queryWrapper);
        Map<String,Object> hashMap=new HashMap<>();
        hashMap.put("total",p.getTotal());
        hashMap.put("items",p.getRecords());
        hashMap.put("current",p.getCurrent());
        hashMap.put("size",p.getSize());
        hashMap.put("pages",p.getPages());
        hashMap.put("hasNext",p.hasNext());
        hashMap.put("hasPrevious",p.hasPrevious());

        return hashMap;
    }
}
