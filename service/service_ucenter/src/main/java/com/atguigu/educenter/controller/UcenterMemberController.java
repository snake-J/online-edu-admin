package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        String token=ucenterMemberService.login(member);
        return R.ok().data("token",token);

    }
    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用工具类方法，根据request对象获取头信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据用户id查询用户，返回用户信息对象
    @GetMapping("getInfo/{id}")
    public com.atguigu.commonutils.vo.UcenterMember getMemberInfoById(@PathVariable String id){
        UcenterMember user = ucenterMemberService.getById(id);
        com.atguigu.commonutils.vo.UcenterMember userInfo= new com.atguigu.commonutils.vo.UcenterMember();
        BeanUtils.copyProperties(user,userInfo);
        return userInfo;
    }

    //统计某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count=ucenterMemberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }
}

