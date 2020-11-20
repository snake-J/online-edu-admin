package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-16
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember member) {
        //1.判断登录手机号和密码是否为空，为空抛出异常
        String phone=member.getMobile();
        String pwd=member.getPassword();
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(pwd)){
            throw new GuliException(20001,"登录失败!");
        }
        //2.判断手机号是否正确
        //3.判断手机号是否正确，构造条件，进行查询。
        QueryWrapper<UcenterMember> q1=new QueryWrapper<>();
        q1.eq("mobile",member.getMobile());
        UcenterMember user = baseMapper.selectOne(q1);

        //4.判断查询对象是否为空
        if(user==null){
            throw new GuliException(20001,"登录失败！");
        }
        //5.若不为空，则判断密码是否正确
        //此时的密码与数据库中的是不一样的，数据库中存储的加密过后的密码，而用户输入
        //的是非加密的密码，所有需要对用户输入的密码先进行加密再比对
        if(!(MD5.encrypt(pwd).equals(user.getPassword()))){
            throw new GuliException(20001,"登录失败！");
        }
        //6.密码正确的话，再判断一下是否被禁用
        if(user.getIsDisabled()){
           throw new GuliException(20001,"登录失败");
        }
        //7.利用JWT生成token
        String jwtToken = JwtUtils.getJwtToken(user.getId(),user.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //1.获取注册信息
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();
        //2.判断是否为空
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)
        ){
            throw new GuliException(20001,"注册失败");
        }
        //3.判断验证码，先获取redis里的验证码
        String s = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(s)){
            throw new GuliException(20001,"注册失败");
        }
        //4.查询数据库中是否存在相同的手机号
        QueryWrapper<UcenterMember> q1=new QueryWrapper<>();
        q1.eq("mobile",mobile);
        Integer flag = baseMapper.selectCount(q1);
        if(flag>0){
            throw new GuliException(20001,"注册失败");
        }
        //5.数据添加到数据库中
        UcenterMember user=new UcenterMember();
        user.setMobile(mobile);
        user.setNickname(nickname);
        user.setPassword(MD5.encrypt(password));
        user.setIsDisabled(false);
        user.setAvatar("https://edu-teacherimg.oss-cn-beijing.aliyuncs.com/2020/10/14/07bec10a135c4775bb2b7e8f5e16fb1b02.jpg");
        baseMapper.insert(user);
    }

    @Override
    public UcenterMember getMemberByOpenid(String openid) {
        QueryWrapper<UcenterMember> q=new QueryWrapper<>();
        q.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(q);
        return member;
    }

    @Override
    public Integer countRegister(String day) {
        Integer count=baseMapper.countRegister(day);
        return count;
    }
}
