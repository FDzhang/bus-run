package com.example.busrun.demo;

import com.example.busrun.demo.constant.BusConstant;
import com.example.busrun.demo.entity.*;
import com.example.busrun.demo.service.BusService;
import com.example.busrun.demo.service.SiteService;

import java.util.*;

/**
 * @author : zxq
 * @create : 2022/3/17 19:56
 */
public class DemoApplication {

    private final static Long RUN_TIME_LIMIT = 300 * 60L;


    public static void main(String[] args) {
        TimeClock clock = new TimeClock();

        LinkedHashMap<Integer, BusSite> busSites = DemoApplication.buildBusSites(clock);
        LinkedHashMap<Integer, Route> routeMap = DemoApplication.buildRoute(busSites);
        List<Bus> busList = DemoApplication.buildBusList(clock, routeMap);

        while (clock.getClock() < DemoApplication.RUN_TIME_LIMIT) {
            new SiteService(clock, routeMap).run();

            for (Bus bus : busList) {
                new BusService(bus).run();
            }

            clock.getTime().getAndAdd(60);
        }
        System.out.println("Finished all");

        for (Bus bus : busList) {
            bus.printRunLog();
        }
        System.err.println("公交车(NAME)\t总载客人数\t总运行时间(分钟)\t总行驶时间(分钟)");
        for (Bus bus : busList) {
            System.err.println(bus.toString());
        }

        System.err.println("站点(code)\t人数");
        for (BusSite site : busSites.values()) {
            System.err.println(site.toString());
        }
    }


    /**
     * 创建公交
     */
    public static List<Bus> buildBusList(TimeClock busClock, LinkedHashMap<Integer, Route> routeMap) {
        char raw = 'A';
        List<Bus> busList = new ArrayList<>();
        long start = 0;
        for (int i = 0; i < 5; i++) {
            Bus bus = new Bus(busClock, String.valueOf(raw++), routeMap, 0);
            bus.setStartTime(start++ * BusConstant.CYCLE);
            busList.add(bus);
        }
        start = 0;
        for (int i = 0; i < 5; i++) {
            Bus bus = new Bus(busClock, String.valueOf(raw++), routeMap, 1);
            bus.setStartTime(start++ * BusConstant.CYCLE);
            busList.add(bus);
        }
        return busList;
    }

    /**
     * 创建路线
     */
    private static LinkedHashMap<Integer, Route> buildRoute(LinkedHashMap<Integer, BusSite> busSites) {
        LinkedHashMap<Integer, Route> res = new LinkedHashMap<>();
        res.put(0, DemoApplication.buildRouteA(busSites));
        res.put(1, DemoApplication.buildRouteB(busSites));
        return res;
    }

    /**
     * 创建站点 [1,15]
     */
    private static LinkedHashMap<Integer, BusSite> buildBusSites(TimeClock time) {
        LinkedHashMap<Integer, BusSite> busSites = new LinkedHashMap<>(16);
        for (int i = 1; i <= 15; i++) {
            busSites.put(i, new BusSite(i, time));
        }
        return busSites;
    }

    public static Route buildRouteA(LinkedHashMap<Integer, BusSite> busSites) {
        LinkedHashMap<Integer, RoadSection> roadSectionMap = new LinkedHashMap<>(16);
        roadSectionMap.put(1, new RoadSection(1, 2, 5));
        roadSectionMap.put(2, new RoadSection(2, 3, 6));
        roadSectionMap.put(3, new RoadSection(3, 4, 7));
        roadSectionMap.put(4, new RoadSection(4, 5, 8));
        roadSectionMap.put(5, new RoadSection(5, 6, 4));
        roadSectionMap.put(6, new RoadSection(6, 7, 3));
        roadSectionMap.put(7, new RoadSection(7, 8, 6));
        roadSectionMap.put(8, new RoadSection(8, 9, 5));
        roadSectionMap.put(9, new RoadSection(9, 10, 6));
        roadSectionMap.put(10, new RoadSection(10, 11, 7));
        roadSectionMap.put(11, new RoadSection(11, 12, 4));
        roadSectionMap.put(12, new RoadSection(12, 13, 3));
        roadSectionMap.put(13, new RoadSection(13, 14, 6));
        roadSectionMap.put(14, new RoadSection(14, 15, 3));
        return new Route(0, busSites, roadSectionMap);
    }


    public static Route buildRouteB(LinkedHashMap<Integer, BusSite> busSites) {
        LinkedHashMap<Integer, RoadSection> roadSectionMap = new LinkedHashMap<>(16);
        roadSectionMap.put(15, new RoadSection(15, 14, 4));
        roadSectionMap.put(14, new RoadSection(14, 13, 5));
        roadSectionMap.put(13, new RoadSection(13, 12, 4));
        roadSectionMap.put(12, new RoadSection(12, 11, 5));
        roadSectionMap.put(11, new RoadSection(11, 10, 4));
        roadSectionMap.put(10, new RoadSection(10, 9, 7));
        roadSectionMap.put(9, new RoadSection(9, 8, 3));
        roadSectionMap.put(8, new RoadSection(8, 7, 5));
        roadSectionMap.put(7, new RoadSection(7, 6, 4));
        roadSectionMap.put(6, new RoadSection(6, 5, 3));
        roadSectionMap.put(5, new RoadSection(5, 4, 6));
        roadSectionMap.put(4, new RoadSection(4, 3, 5));
        roadSectionMap.put(3, new RoadSection(3, 2, 7));
        roadSectionMap.put(2, new RoadSection(2, 1, 4));
        return new Route(1, busSites, roadSectionMap);
    }


}
