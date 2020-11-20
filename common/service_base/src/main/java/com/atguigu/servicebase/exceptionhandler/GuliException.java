package com.atguigu.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor     //生成全参构造方法
@NoArgsConstructor  //生成无参构造
public class GuliException extends RuntimeException{
    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "提示信息")
    private String msg;
}
