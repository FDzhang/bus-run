package com.example.busrun.demo.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 路线
 * @author : zxq
 * @create : 2022/3/17 18:12
 */
@Data
public class Route {
    /**
     * 站点编号，站点
     */
    private Map<Integer, BusSite> busSiteMap;

    /**
     * 站点编号，路段
     */
    private Map<Integer, RoadSection> roadSectionMap;

    public Route() {
    }

    public Route(Map<Integer, BusSite> busSiteMap, Map<Integer, RoadSection> roadSectionMap) {
        this.busSiteMap = busSiteMap;
        this.roadSectionMap = roadSectionMap;
    }
}
