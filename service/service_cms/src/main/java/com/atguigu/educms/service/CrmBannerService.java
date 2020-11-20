package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
public interface CrmBannerService extends IService<CrmBanner> {
    //获取所有banner
    List<CrmBanner> getAllBanner();
    //根据id删除banner，同时也把oss上的也删除
    void removeBannerAndOssById(String id);
}
