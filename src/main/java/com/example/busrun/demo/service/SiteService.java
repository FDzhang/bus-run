package com.example.busrun.demo.service;

import com.example.busrun.demo.entity.Passenger;
import com.example.busrun.demo.entity.RoadSection;
import com.example.busrun.demo.entity.Route;
import com.example.busrun.demo.entity.TimeClock;
import com.example.busrun.demo.utils.IRandomUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 站点服务
 * 定时产生乘客
 *
 * @author : zxq
 * @create : 2022/3/17 21:55
 */
public class SiteService implements Runnable {
    public SiteService(TimeClock clock, LinkedHashMap<Integer, Route> routeMap) {
        this.clock = clock;
        this.routeMap = routeMap;
    }

    /**
     * 时钟
     */
    private TimeClock clock;
    /**
     * 站点List
     */
    private LinkedHashMap<Integer, Route> routeMap;


    @Override
    public void run() {
        // 每5分钟
        if (clock.getClock() % (5 * 60) == 0) {
            for (int i = 0; i < 5; i++) {
                randomPassenger();
            }
        }
    }

    public void randomPassenger() {
        int routeCode = IRandomUtil.randomNumber(0, 2);
        // 随机路线
        Route route = routeMap.get(routeCode);
        // 随机 起点、终点
        ArrayList<RoadSection> roadSections = new ArrayList<>(route.getRoadSectionMap().values());
        List<Integer> sites = new ArrayList<>();
        for (RoadSection r : roadSections) {
            sites.add(r.getCurSite());
        }
        sites.add(roadSections.get(roadSections.size() - 1).getNextSite());

        List<Integer> site2 = IRandomUtil.randomEleList(sites);
        Passenger p = new Passenger(IRandomUtil.fastSimpleUUID(), routeCode, site2.get(0), site2.get(1));

        // 入站
        route.getBusSiteMap().get(site2.get(0)).getPassengers().add(p);
    }
}
