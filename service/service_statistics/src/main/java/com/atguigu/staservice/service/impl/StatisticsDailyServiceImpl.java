package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-10
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public void countRegister(String day) {

        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        R registerR = ucenterClient.countRegister(day);
        Integer count = (Integer) registerR.getData().get("countRegister");

        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO


        StatisticsDaily statisticsDaily=new StatisticsDaily();
        statisticsDaily.setDateCalculated(day);
        statisticsDaily.setRegisterNum(count);
        statisticsDaily.setVideoViewNum(videoViewNum);
        statisticsDaily.setLoginNum(loginNum);
        statisticsDaily.setCourseNum(courseNum);

        baseMapper.insert(statisticsDaily);

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> queryWrapper=new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        //精准确认到需要查询哪个数据
        queryWrapper.select("date_calculated",type);
        List<StatisticsDaily> dayList = baseMapper.selectList(queryWrapper);

        //创建 日期和数据的 list集合
        List<Integer> dataList=new ArrayList<>();
        List<String> dateList=new ArrayList<>();

        for (int i = 0; i < dayList.size(); i++) {
            StatisticsDaily sta = dayList.get(i);
            dateList.add(sta.getDateCalculated());
            switch (type){
                case "login_num":
                    dataList.add(sta.getLoginNum());
                    break;
                case "register_num":
                    dataList.add(sta.getRegisterNum());
                    break;
                case "video_view_num":
                    dataList.add(sta.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(sta.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> hashMap=new HashMap<>();
        hashMap.put("dataList",dataList);
        hashMap.put("dateList",dateList);
        return hashMap;
    }
}
