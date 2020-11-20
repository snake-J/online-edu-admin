package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-13
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if (teacherQuery == null){
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }
        //获取teacherQuery里的数据
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();

        //判断是否有数据，有则添加上对应的条件
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_modified",end);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Cacheable(value = "teacher",key = "'SelectTeacherList'")
    @Override
    public List<EduTeacher> getTeacherFront() {
        QueryWrapper<EduTeacher> q2=new QueryWrapper<>();
        q2.orderByDesc("id");
        q2.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(q2);
        return eduTeachers;

    }

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByDesc("id");

        baseMapper.selectPage(pageTeacher,queryWrapper);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("total",pageTeacher.getTotal());
        hashMap.put("items",pageTeacher.getRecords());
        hashMap.put("pages",pageTeacher.getPages());
        hashMap.put("current",pageTeacher.getCurrent());
        hashMap.put("size",pageTeacher.getSize());
        hashMap.put("hasNext",pageTeacher.hasNext());
        hashMap.put("hasPrevious",pageTeacher.hasPrevious());

        return hashMap;
    }
}
