package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "Course查询对象", description = "课程查询对象封装")
@Data
public class CourseListVo {
    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "课程发布状态")
    private String status;

    @ApiModelProperty(value = "一级类别id")
    private String subjectParentId;

    @ApiModelProperty(value = "二级类别id")
    private String subjectId;

}
