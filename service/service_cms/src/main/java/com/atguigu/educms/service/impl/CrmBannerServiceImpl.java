package com.atguigu.educms.service.impl;

import com.atguigu.educms.client.OssClient;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.mapper.CrmBannerMapper;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {
    @Autowired
    private OssClient ossClient;
    @Cacheable(value = "banner",key = "'bannerList'")
    @Override
    public List<CrmBanner> getAllBanner() {
        QueryWrapper<CrmBanner> q1=new QueryWrapper<>();
        q1.orderByDesc("sort");
        q1.last("limit 2");
        List<CrmBanner> crmBanners = baseMapper.selectList(q1);
        return crmBanners;
    }
    @CacheEvict(value = "banner", allEntries=true)
    @Override
    public void removeBannerAndOssById(String id) {
        QueryWrapper<CrmBanner> q1=new QueryWrapper<>();
        q1.eq("id",id);
        CrmBanner crmBanner = baseMapper.selectOne(q1);
        String imageUrl = crmBanner.getImageUrl();
        if(!StringUtils.isEmpty(imageUrl)){
            String imageName= org.apache.commons.lang.StringUtils.substringAfter(imageUrl,"com/");
            ossClient.removeImg(imageName);
        }
        baseMapper.deleteById(id);
    }
}
