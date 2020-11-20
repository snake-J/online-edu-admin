package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-09-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        //获取文件
        try {
            //文件输入流
            InputStream excelFile = file.getInputStream();
            EasyExcel.read(excelFile, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> findAll() {
        //获取一级对象列表
        QueryWrapper<EduSubject> qw1=new QueryWrapper<>();
        qw1.eq("parent_id","0");
        List<EduSubject> oneSubject = baseMapper.selectList(qw1);
        //获取二级对象列表
        QueryWrapper<EduSubject> qw2=new QueryWrapper<>();
        qw2.ne("parent_id","0");
        List<EduSubject> twoSubject = baseMapper.selectList(qw2);

        //最终返回数据
        ArrayList<OneSubject> finalList=new ArrayList<>();

        //封装一级对象
        for (int i = 0; i < oneSubject.size(); i++) {
            //获取eduSubject对象
            EduSubject eduSubject = oneSubject.get(i);
            //创建一级对象
            OneSubject oneObj=new OneSubject();
            //使用工具类，将eduSubject的get到的值，赋给oneObj
            BeanUtils.copyProperties(eduSubject,oneObj);
            finalList.add(oneObj);

            //封装二级对象
            //创建二级对象列表
            ArrayList<TwoSubject> list=new ArrayList<>();
            for (int j = 0; j < twoSubject.size(); j++) {
                //创建eduSubject1对象
                EduSubject eduSubject1 = twoSubject.get(j);
               /* 判断二级对象的parentid是否与一级对象的id相等
               *  如果相等，则证明于此二级对象属于此时的一级对象的children
               * */

                if(eduSubject.getId().equals(eduSubject1.getParentId())){
                    //创建二级对象
                    TwoSubject twoObj=new TwoSubject();
                    BeanUtils.copyProperties(eduSubject1,twoObj);
                    list.add(twoObj);
                }
            }
            oneObj.setChildren(list);

        }
        return finalList;

    }
}
