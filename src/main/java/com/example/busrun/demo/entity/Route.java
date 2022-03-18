package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusSiteTypeEnum;
import lombok.Data;

import java.util.Map;

/**
 * 路线
 * @author : zxq
 * @create : 2022/3/17 18:12
 */
@Data
public class Route {

    /**
     * 起点
     */
    private Integer startSiteCode;

    /**
     * 终点
     */
    private Integer endSiteCode;

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

    public BusSiteTypeEnum checkBusSiteType(int busSiteCode) {
        if (this.startSiteCode == busSiteCode) {
            return BusSiteTypeEnum.START;
        } else if (this.endSiteCode == busSiteCode) {
            return BusSiteTypeEnum.END;
        } else {
            return BusSiteTypeEnum.NORMAL;
        }
    }

    public boolean isStart(int busSiteCode) {
        return this.startSiteCode == busSiteCode;
    }

    public boolean isEnd(int busSiteCode) {
        return this.endSiteCode == busSiteCode;
    }
}
