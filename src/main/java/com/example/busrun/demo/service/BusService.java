package com.example.busrun.demo.service;

import com.example.busrun.demo.constant.BusConstant;
import com.example.busrun.demo.constant.BusSiteTypeEnum;
import com.example.busrun.demo.entity.*;
import com.example.busrun.demo.utils.RandomUtils;

import java.util.List;
import java.util.Map;

/**
 * 公交车 服务
 * 在路线上运行
 *
 * @author : zxq
 * @create : 2022/3/17 21:51
 */
public class BusService implements Runnable {

    private TimeClock clock;
    private Bus bus;
    private Map<Integer, Route> routeMap;
    private Long limit;

    public BusService(TimeClock clock, Bus bus, Map<Integer, Route> routeMap, Long limit) {
        this.clock = clock;
        this.bus = bus;
        this.routeMap = routeMap;
        this.limit = limit;
    }

    @Override
    public void run() {
        try {
            busRun(clock, bus, routeMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param clock    时钟
     * @param bus      公交
     * @param routeMap 路线
     */
    public void busRun(TimeClock clock, Bus bus, Map<Integer, Route> routeMap) throws InterruptedException {
        if (clock.getTime().get() == bus.getTime()) {
            BusSite curBusSite = routeMap.get(bus.getRouteCode()).getBusSiteMap().get(bus.getSiteCode());
            RoadSection roadSection = routeMap.get(bus.getRouteCode()).getRoadSectionMap().get(bus.getSiteCode());

            if (curBusSite == null || roadSection == null) {
                System.err.println(bus);
                return ;
            }

            // 入站日志
            bus.enterSiteRunLog(curBusSite.getSiteType());
            // 概率性故障
            if (RandomUtils.busFaultRandom()) {
                // 故障乘客下车
                List<Passenger> faultOffPassengers = bus.faultOffPassengers();
                curBusSite.addPassengers(faultOffPassengers);
                bus.getPassengersMap().clear();
            } else {
                // 乘客下车
                List<Passenger> offPassengers = bus.offPassengers();

                // 乘客上车
                int remain = BusConstant.CAPACITY - bus.getPassengersMap().size();
                List<Passenger> sitePassengers = curBusSite.reducePassengers(remain);
                bus.upPassengers(sitePassengers);

                // 出站日志
                bus.outSiteRunLog(curBusSite.getSiteType(), offPassengers.size(), sitePassengers.size());

                // 上下车时间
                bus.setTime(bus.getTime() + BusConstant.UP_OFF_TIME);
                // 添加一个随机时间
                bus.setTime(bus.getTime() + RandomUtils.driveTimeRandom());

                // 终点站转向
                if (curBusSite.getSiteType().equals(BusSiteTypeEnum.END)) {
                    bus.setRouteCode((bus.getRouteCode() + 1) % 2);
                    bus.setTime(bus.getTime() + BusConstant.CYCLE);
                } else {
                    // 下一站的预计行程时间
                    bus.setTime(bus.getTime() + roadSection.getDistance());
                }
            }
        }
    }
}
