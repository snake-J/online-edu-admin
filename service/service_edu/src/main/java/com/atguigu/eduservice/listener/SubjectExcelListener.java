package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }
    public SubjectExcelListener() {
    }

    @Override
    //一行一行读取,第一个值一级分类，第二个值二级分类
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData==null){
            throw new GuliException(20001,"文件数据为空！");
        }
        EduSubject eduOneSubject = this.existOneSubject(subjectData.getOneSubjectName(), eduSubjectService);
        if(eduOneSubject==null){
            eduOneSubject=new EduSubject();
            eduOneSubject.setTitle(subjectData.getOneSubjectName());
            eduOneSubject.setParentId("0");
            eduSubjectService.save(eduOneSubject);
        }
        String pid = eduOneSubject.getId();
        //二级分类
        EduSubject eduTwoSubject = this.existTwoSubject(subjectData.getTwoSubjectName(), eduSubjectService, pid);
        if(eduTwoSubject==null){
            eduTwoSubject=new EduSubject();
            eduTwoSubject.setTitle(subjectData.getTwoSubjectName());
            eduTwoSubject.setParentId(pid);
            eduSubjectService.save(eduTwoSubject);
        }

    }
    //判断一级分类不能重复添加
    private EduSubject existOneSubject(String name,EduSubjectService eduSubjectService){
        QueryWrapper<EduSubject> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id","0");
        EduSubject one = eduSubjectService.getOne(queryWrapper);
        return one;
    }
    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(String name,EduSubjectService eduSubjectService,String pid){
        QueryWrapper<EduSubject> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id",pid);
        EduSubject two = eduSubjectService.getOne(queryWrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
