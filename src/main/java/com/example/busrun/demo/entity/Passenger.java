package com.example.busrun.demo.entity;

import lombok.Data;

/**
 * @author : zxq
 * @create : 2022/3/17 17:30
 */
@Data
public class Passenger {
    /**
     * 所在站点
     */
    private Integer curSite;
    /**
     * 目标站点
     */
    private Integer targetSite;

    /**
     * 路线 （所在站点+目标站点决定路线）
     */
    private Integer routeCode;

    public Passenger(Integer curSite, Integer targetSite, Integer routeCode) {
        this.curSite = curSite;
        this.targetSite = targetSite;
        this.routeCode = routeCode;
    }
}
