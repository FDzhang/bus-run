package com.example.busrun.demo;

import com.example.busrun.demo.entity.*;
import com.example.busrun.demo.service.BusService;
import com.example.busrun.demo.service.SiteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : zxq
 * @create : 2022/3/17 19:56
 */
public class DemoApplication {

    public static void main(String[] args) {
        TimeClock siteClock = new TimeClock();
        Map<Integer, BusSite> busSites = DemoApplication.buildBusSites(siteClock);
        Map<Integer, Route> routeMap = DemoApplication.buildRoute(busSites);
        List<Bus> busList = DemoApplication.buildBusList(5, 5);
        Long limit = 300 * 60L;

        System.err.println("init finish");

        TimeClock timeClock = new TimeClock();

        while (timeClock.getTime().get() < limit) {
            new SiteService(timeClock, siteClock, new ArrayList<>(busSites.values())).run();

            for (Bus bus : busList) {
                new BusService(timeClock, bus, routeMap).run();
            }

            timeClock.getTime().getAndIncrement();
            System.err.println(timeClock.getTime().get());
        }
        System.out.println("Finished all");


        for (Bus bus : busList) {
            bus.printRunLog();
        }
        System.err.println("公交车(NAME)\t总载客人数\t总运行时间(分钟)\t总行驶时间(分钟)");
        for (Bus bus : busList) {
            System.err.println(bus.toString());
        }

        System.err.println("站点(code)\t路线:人数...");
        for (BusSite site : busSites.values()) {
            System.err.println(site.toString());
        }
    }

    private static Map<Integer, Route> buildRoute(Map<Integer, BusSite> busSites) {
        Map<Integer, Route> res = new HashMap<>();
        res.put(0, DemoApplication.buildRouteA(busSites));
        res.put(1, DemoApplication.buildRouteB(busSites));
        return res;
    }


    public static List<Bus> buildBusList(int up, int down) {
        char raw = 'A';
        List<Bus> busList = new ArrayList<>();
        for (int i = 0; i < up; i++) {
            busList.add(new Bus(0, String.valueOf(raw++), 1));
        }
        for (int i = 0; i < down; i++) {
            busList.add(new Bus(1, String.valueOf(raw++), 15));
        }
        return busList;
    }


    private static Map<Integer, BusSite> buildBusSites(TimeClock time) {
        Map<Integer, BusSite> busSites = new HashMap<>(16);
        for (int i = 1; i <= 15; i++) {
            busSites.put(i, new BusSite(i, time));
        }
        return busSites;
    }

    public static Route buildRouteA(Map<Integer, BusSite> busSites) {
        Map<Integer, RoadSection> roadSectionMap = new HashMap<>(16);
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
        roadSectionMap.put(15, new RoadSection(15, 15, 0));

        Route route = new Route(busSites, roadSectionMap);
        route.setStartSiteCode(1);
        route.setEndSiteCode(15);
        return route;
    }


    public static Route buildRouteB(Map<Integer, BusSite> busSites) {
        Map<Integer, RoadSection> roadSectionMap = new HashMap<>(16);
        roadSectionMap.put(1, new RoadSection(1, 1, 0));
        roadSectionMap.put(2, new RoadSection(2, 1, 4));
        roadSectionMap.put(3, new RoadSection(3, 2, 7));
        roadSectionMap.put(4, new RoadSection(4, 3, 5));
        roadSectionMap.put(5, new RoadSection(5, 4, 6));
        roadSectionMap.put(6, new RoadSection(6, 5, 3));
        roadSectionMap.put(7, new RoadSection(7, 6, 4));
        roadSectionMap.put(8, new RoadSection(8, 7, 5));
        roadSectionMap.put(9, new RoadSection(9, 8, 3));
        roadSectionMap.put(10, new RoadSection(10, 9, 7));
        roadSectionMap.put(11, new RoadSection(11, 10, 4));
        roadSectionMap.put(12, new RoadSection(12, 11, 5));
        roadSectionMap.put(13, new RoadSection(13, 12, 4));
        roadSectionMap.put(14, new RoadSection(14, 13, 5));
        roadSectionMap.put(15, new RoadSection(15, 14, 4));
        Route route = new Route(busSites, roadSectionMap);
        route.setStartSiteCode(15);
        route.setEndSiteCode(1);
        return route;
    }


}
