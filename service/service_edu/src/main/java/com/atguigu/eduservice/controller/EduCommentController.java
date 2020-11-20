package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMember;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-03
 */
@RestController
@RequestMapping("/eduservice/educomment")
//@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private UcenterClient ucenterClient;

    @ApiOperation(value = "评论分页列表")
    @GetMapping("getCommentList/{page}/{limit}")
    public R index(@PathVariable Long page,@PathVariable Long limit,String courseId){
        Page<EduComment> p=new Page<>(page,limit);
        Map<String,Object> map=eduCommentService.getCommentList(p,courseId);
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("save")
    public R save(@RequestBody EduComment eduComment, HttpServletRequest request){
        //通过request获取token，再通过jwt解密token，获取其中的用户id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        //判断用户id是否为空
        if(StringUtils.isEmpty(userId)){
            return R.error().message("请先登陆再评论！");
        }
        //若不为空，则向eduComment设置用户id
        eduComment.setMemberId(userId);

        //根据用户id查询用户信息
        UcenterMember userInfo = ucenterClient.getMemberInfoById(userId);
        //获取其中信息并赋给eduComment
        eduComment.setNickname(userInfo.getNickname());
        eduComment.setAvatar(userInfo.getAvatar());

        //保存评论相关
        eduCommentService.save(eduComment);

        return R.ok();

    }


}

