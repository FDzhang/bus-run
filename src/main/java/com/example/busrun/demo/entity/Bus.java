package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusConstant;
import com.example.busrun.demo.constant.BusStatusEnum;
import com.example.busrun.demo.utils.IRandomUtil;
import com.example.busrun.demo.utils.TimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公交车
 *
 * @author : zxq
 * @create : 2022/3/17 19:44
 */
@Data
public class Bus {

    public Bus() {
    }

    public Bus(TimeClock busClock, String name, LinkedHashMap<Integer, Route> routeMap, int startCode) {
        this.busClock = busClock;
        this.name = name;
        this.routeMap = routeMap;

        this.routeCode = startCode;
        this.nextSiteCode = routeMap.get(this.routeCode).getStartSiteCode();
    }


    // -----------------------


    /**
     * 标准时钟
     */
    private TimeClock busClock;

    /**
     * 公交车名称
     */
    private String name;

    /**
     * 公交车状态
     */
    private BusStatusEnum busStatus = BusStatusEnum.NORMAL;


    //--------------------------------

    /**
     * 总载客人数
     */
    private Integer totalPassengers = 0;
    /**
     * 总行驶时间(秒)
     */
    private Long totalDriveTime = 0L;

    /**
     * 总运行时间(秒)
     */
    private Long totalRunningTime = 0L;

    /**
     * 公交车运行日志
     */
    private List<BusRunLog> busRunLogList = new ArrayList<>();

    // -------------------------------

    /**
     * 当前路线 （上行(从小到大)：0， 下行（从大到小）：1）
     */
    private Integer routeCode;

    /**
     * 可以运行的路线
     * - <路线编号，路线>
     */
    private Map<Integer, Route> routeMap;

    /**
     * next站点编号
     */
    private Integer nextSiteCode;

    /**
     * 预计到达时间
     */
    private Long expectedArriveTime = 0L;

    /**
     * 最近一次出站时间
     */
    private Long outSiteTime = 0L;

    /**
     * 车上的乘客
     * - <乘客编号，乘客>
     */
    private LinkedHashMap<String, Passenger> passengerMap = new LinkedHashMap<>();

    // -------------------------------------

    /**
     * 设置发车时间
     */
    public void setStartTime(Long startTime) {
        this.expectedArriveTime = startTime;
    }

    /**
     * 入站
     * - 入站日志
     */
    public void enterBusSite() {
        enterSiteRunLog();
    }

    /**
     * 入站日志
     */
    public void enterSiteRunLog() {
        Route route = this.routeMap.get(this.getRouteCode());

        if (route.isStart(this.nextSiteCode)) {
            return;
        }

        String action;
        if (route.isEnd(this.nextSiteCode)) {
            action = "抵达终点站";
        } else {
            action = String.format("到达 %02d 站", this.nextSiteCode);
        }
        totalDriveTime += (busClock.getClock() - outSiteTime);
        totalRunningTime = busClock.getClock();
        busRunLogList.add(new BusRunLog(this.busClock.getClock(), action));
    }

    /**
     * 通知乘客下车
     * - 乘客下车
     * - 故障下车
     */
    public List<Passenger> notifyPassengerOff() {
        if (BusStatusEnum.FAULT.equals(this.busStatus)) {
            return notifyPassengerOffFault();
        }

        return notifyPassengerOffNormal();
    }

    private List<Passenger> notifyPassengerOffNormal() {
        List<Passenger> res = new ArrayList<>();
        // 需要下车的乘客
        for (Passenger p : this.passengerMap.values()) {
            if (p.getTargetSite().equals(this.nextSiteCode)) {
                res.add(p);
            }
        }
        // 删除下车的乘客
        for (Passenger p : res) {
            p.offBus(this);
        }
        return res;
    }

    private List<Passenger> notifyPassengerOffFault() {
        // 需要下车的乘客
        List<Passenger> res = new ArrayList<>(passengerMap.values());

        // 删除下车的乘客
        for (Passenger p : passengerMap.values()) {
            p.offBus(this);
        }
        return res;
    }

    /**
     * 车辆故障
     * - 标记故障
     * - 故障日志
     */
    public void busFault() {
        this.busStatus = BusStatusEnum.FAULT;
        faultRunLog();

        totalDriveTime += (busClock.getClock() - outSiteTime);
        totalRunningTime = busClock.getClock();
    }

    /**
     * 故障日志
     */
    private void faultRunLog() {
        String action = String.format("在 %02d 站故障，下客 %d 人", this.nextSiteCode, passengerMap.size());
        busRunLogList.add(new BusRunLog(this.busClock.getClock(), action));
    }

    /**
     * 通知站点到站
     * - 非终点则通知
     */
    public List<Passenger> notifyBusSiteArrive() {
        Route route = this.routeMap.get(this.getRouteCode());

        if (!route.isEnd(this.nextSiteCode)) {
            BusSite busSite = route.getBusSiteMap().get(this.nextSiteCode);
            // 公交到站、通知乘客上车
            return busSite.notifyPassengerUp(this);
        }
        return new ArrayList<>();
    }

    /**
     * 变更车站信息
     * - 是否需要改变路线和等待发车
     * - 改变下一站站点编号
     * - 下一站预计到达时间 (上下车时间+距离时间+误差时间)
     */
    public void busToNext() {
        Route route = this.routeMap.get(this.getRouteCode());
        this.expectedArriveTime += BusConstant.UP_OFF_TIME;
        this.expectedArriveTime += IRandomUtil.driveTimeRandom();

        if (route.isEnd(this.nextSiteCode)) {
            // 目前只有 0：上行， 1：下行
            this.routeCode = (this.routeCode + 1) % 2;
            this.expectedArriveTime += BusConstant.CYCLE;

            Route newRoute = this.routeMap.get(this.getRouteCode());
            this.nextSiteCode = newRoute.getBusSiteMap().values().iterator().next().getSiteCode();
        } else {
            RoadSection roadSection = route.getRoadSectionMap().get(this.nextSiteCode);
            this.nextSiteCode = roadSection.getNextSite();
            this.expectedArriveTime += roadSection.getDistance();
        }
    }

    /**
     * 出站
     * - 改变最近一次出站时间
     * - 出站日志
     */
    public void outBusSite(Integer offNumber, Integer upNumber) {
        outSiteRunLog(offNumber, upNumber);
    }

    /**
     * 出站日志
     */
    private void outSiteRunLog(Integer offNumber, Integer upNumber) {
        Route route = this.routeMap.get(this.getRouteCode());

        String action;
        if (route.isStart(this.nextSiteCode)) {
            action = String.format("从 %02d 站发车，乘客 %d 人", this.nextSiteCode, upNumber);
            this.outSiteTime = this.getBusClock().getClock();
        } else if (route.isEnd(this.nextSiteCode)) {
            action = String.format("下客 %d 人，等待发车", offNumber);
            this.outSiteTime = this.getBusClock().getClock() + BusConstant.CYCLE;
        } else {
            action = String.format("下客 %d 人，上客 %d 人，继续出发", offNumber, upNumber);
            this.outSiteTime = this.getBusClock().getClock();
        }
        busRunLogList.add(new BusRunLog(this.busClock.getClock(), action));
    }


    /**
     * 日志打印
     */
    public void printRunLog() {
        System.err.println("公交车 " + this.name);
        System.err.println("时间 \t 动作");
        for (BusRunLog busRunLog : busRunLogList) {
            System.err.println(TimeUtil.long2TimeStr(busRunLog.getTime()) + " \t " + busRunLog.getAction());
        }
        System.err.println();
    }

    @Override
    public String toString() {
        // 公交车(NAME) 总载客人数 总运行时间(分钟) 总行驶时间(分钟)
        return String.format("%10s\t%10s\t%10s\t%10s", this.name, this.totalPassengers, this.totalRunningTime / 60, this.totalDriveTime / 60);
    }
}
