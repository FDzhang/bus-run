package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusSiteTypeEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路线
 *
 * @author : zxq
 * @create : 2022/3/17 18:12
 */
@Data
public class Route {

    public Route() {
    }

    public Route(Integer routeCode, LinkedHashMap<Integer, BusSite> busSiteMap, LinkedHashMap<Integer, RoadSection> roadSectionMap) {
        this.routeCode = routeCode;
        this.busSiteMap = busSiteMap;
        this.roadSectionMap = roadSectionMap;

        this.startSiteCode  = roadSectionMap.entrySet().iterator().next().getKey();
        try {
            Field tail = roadSectionMap.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            this.endSiteCode = ((Map.Entry<Integer, RoadSection>) tail.get(roadSectionMap)).getValue().getNextSite();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编号
     */
    private Integer routeCode;

    // ----------------
    /**
     * 起点
     */
    private Integer startSiteCode;

    /**
     * 终点
     */
    private Integer endSiteCode;

    /**
     * 包含的站点
     * - <站点编号,站点>
     */
    private LinkedHashMap<Integer, BusSite> busSiteMap;

    /**
     * 包含的路段
     * - <路段开头编号,路段>
     */
    private LinkedHashMap<Integer, RoadSection> roadSectionMap;


    // -------------------

    public boolean isStart(int busSiteCode) {
        return this.startSiteCode == busSiteCode;
    }

    public boolean isEnd(int busSiteCode) {
        return this.endSiteCode == busSiteCode;
    }
}
