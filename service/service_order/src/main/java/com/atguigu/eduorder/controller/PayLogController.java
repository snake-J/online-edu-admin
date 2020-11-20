package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-08
 */
@RestController
@RequestMapping("/eduorder/paylog")
//@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //三、生成微信支付的二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        //返回信息
        Map map=payLogService.createNative(orderNo);
        System.out.println("##############二维码集合:"+map);
        return R.ok().data(map);
    }

    //四、查询订单支付状态
    @GetMapping("queryPayState/{orderNo}")
    public R queryPayState(@PathVariable String orderNo){
       Map<String,String> map= payLogService.queryPayStatus(orderNo);
        System.out.println("##############支付状态集合:"+map);
       if(map==null){
           return R.error().message("支付出错了！");
       }
       //如果map不为空，判断map中的trade_state是否等SUCCESS，是则表明支付成功
       if(map.get("trade_state").equals("SUCCESS")){
           //向支付记录添加记录,更新订单列表的状态
           payLogService.updateOrderStatus(map);
           return R.ok().message("支付成功！");
       }
       return R.ok().code(25000).message("支付中...");
    }



}

