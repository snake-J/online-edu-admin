package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
@RestController
@RequestMapping("/educms/banneradmin")
//@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;

    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){
        Page<CrmBanner> p1=new Page<>(page,limit);
        crmBannerService.page(p1,null);
        return R.ok().data("items",p1.getRecords()).data("total",p1.getTotal());
    }

    @ApiOperation(value = "新增banner")
    @PostMapping("save")
    public R saveBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "根据id获取banner")
    @GetMapping("getBanner/{id}")
    public R getBanner(@PathVariable String id){
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("item",banner);
    }

    @ApiOperation(value = "修改banner")
    @PostMapping("updateBanner")
    @CacheEvict(value = "banner", allEntries=true)
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "删除banner")
    @DeleteMapping("remove/{id}")
    public R removeBanner(@PathVariable String id){
        crmBannerService.removeBannerAndOssById(id);
        return R.ok();
    }



}

