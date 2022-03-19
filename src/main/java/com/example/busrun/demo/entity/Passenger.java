package com.example.busrun.demo.entity;

import lombok.Data;

/**
 * @author : zxq
 * @create : 2022/3/17 17:30
 */
@Data
public class Passenger {
    public Passenger(String passengerCode, Integer routeCode, Integer curSite, Integer targetSite) {
        this.curSite = curSite;
        this.targetSite = targetSite;
        this.routeCode = routeCode;

        this.passengerCode = passengerCode;
    }

    /**
     * 编号
     */
    private String passengerCode;

    // -------------------
    /**
     * 起点站点
     */
    private Integer curSite;
    /**
     * 目标站点
     */
    private Integer targetSite;

    /**
     * 路线编号
     */
    private Integer routeCode;


    // ------------

    /**
     * 上车
     */
    public void upBus(Bus bus) {
        bus.getPassengerMap().put(this.getPassengerCode(), this);
    }

    /**
     * 下车
     */
    public void offBus(Bus bus) {
        bus.getPassengerMap().remove(this.getPassengerCode());

        // 没到目的地
        if (!this.getTargetSite().equals(bus.getNextSiteCode())) {
            Route route = bus.getRouteMap().get(bus.getRouteCode());
            BusSite busSite = route.getBusSiteMap().get(bus.getNextSiteCode());

            busSite.getPassengers().addFirst(this);
        }
    }

}
