package com.example.busrun.demo.service;

import com.example.busrun.demo.constant.BusConstant;
import com.example.busrun.demo.entity.*;
import com.example.busrun.demo.utils.RandomUtil;

import java.util.ArrayList;
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

    public BusService(TimeClock clock, Bus bus, Map<Integer, Route> routeMap) {
        this.clock = clock;
        this.bus = bus;
        this.routeMap = routeMap;
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
            Route route = routeMap.get(bus.getRouteCode());
            BusSite curBusSite = route.getBusSiteMap().get(bus.getSiteCode());
            RoadSection roadSection = route.getRoadSectionMap().get(bus.getSiteCode());

            // 入站日志
            bus.enterSiteRunLog(route.checkBusSiteType(curBusSite.getCode()));

            // 概率性故障
            if (RandomUtil.busFaultRandom()) {
                // 故障乘客下车
                List<Passenger> faultOffPassengers = bus.faultOffPassengers();
                curBusSite.addPassengers(faultOffPassengers, true);
                bus.getPassengersMap().clear();
            } else {
                // 乘客下车
                List<Passenger> offPassengers = bus.offPassengers();
                List<Passenger> sitePassengers = new ArrayList<>();
                if (!route.isEnd(curBusSite.getCode())) {
                    // 乘客上车
                    int remain = BusConstant.CAPACITY - bus.getPassengersMap().size();
                    sitePassengers.addAll(curBusSite.reducePassengers(remain, bus.getRouteCode()));
                    bus.upPassengers(sitePassengers);
                }
                // 上下车时间
                bus.setTime(bus.getTime() + BusConstant.UP_OFF_TIME);

                // 出站日志
                bus.outSiteRunLog(route.checkBusSiteType(curBusSite.getCode()), offPassengers.size(), sitePassengers.size());

                // 添加一个随机时间
                bus.setTime(bus.getTime() + RandomUtil.driveTimeRandom());
                // 终点站转向
                if (route.isEnd(curBusSite.getCode())) {
                    bus.setRouteCode((bus.getRouteCode() + 1) % 2);
                    bus.setTime(bus.getTime() + BusConstant.CYCLE);
                } else {
                    // 下一站的预计行程时间
                    bus.setTime(bus.getTime() + roadSection.getDistance() * 60);
                    bus.setSiteCode(roadSection.getNextSite());
                }
            }
        }
    }
}
