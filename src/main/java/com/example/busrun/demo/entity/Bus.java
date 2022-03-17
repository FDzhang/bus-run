package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusStatusEnum;
import com.example.busrun.demo.utils.TimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * 公交车状态
     */
    private BusStatusEnum busStatus = BusStatusEnum.NORMAL;

    /**
     * 公交车 时间
     */
    private Long time;

    /**
     * 当前站点编号
     */
    private Integer siteCode;

    /**
     * 路线 （上行(从小到大)：0， 下行（从大到小）：1）
     */
    private Integer routeCode;

    /**
     * 公交车名称
     */
    private String name;

    /**
     * 车上的乘客
     * 各个目标站点的乘客
     */
    private Map<Integer, List<Passenger>> passengersMap;

    /**
     * 公交车运行日志
     */
    private List<BusRunLog> busRunLogList;

    /**
     * 总载客人数
     */
    private Integer totalPassengers;

    /**
     * 总运行时间(秒)
     */
    private Integer totalRunningTime;

    /**
     * 从起点出发的时间
     */
    private Integer outSiteTime;

    /**
     * 总行驶时间(秒)
     */
    private Integer totalDriveTime;


    public Bus() {
    }

    public Bus(Integer routeCode, String name, Integer siteCode) {
        this.siteCode = siteCode;
        this.routeCode = routeCode;
        this.name = name;
        this.totalPassengers = 0;
        this.totalRunningTime = 0;
        this.totalDriveTime = 0;
        this.time = 0L;
        this.outSiteTime = 0;

        this.passengersMap = new HashMap<>();
        this.busRunLogList = new ArrayList<>();
    }

    /**
     * 到站下车
     */
    public List<Passenger> offPassengers() {
        List<Passenger> offPassenger = passengersMap.remove(this.getSiteCode());
        return offPassenger == null ? new ArrayList<>() : offPassenger;
    }

    /**
     * 故障下车
     */
    public List<Passenger> faultOffPassengers() {
        List<Passenger> res = new ArrayList<>();
        for (List<Passenger> p : passengersMap.values()) {
            res.addAll(p);
        }

        totalRunningTime = time.intValue();
        this.busStatus = BusStatusEnum.FAULT;
        // 记录日志
        faultRunLog(res.size());
        return res;
    }

    /**
     * 乘客上车
     */
    public void upPassengers(List<Passenger> passengers) {
        for (Passenger passenger : passengers) {
            Integer targetSite = passenger.getTargetSite();

            if (!passengersMap.containsKey(targetSite)) {
                passengersMap.put(targetSite, new ArrayList<>());
            }
            passengersMap.get(targetSite).add(passenger);
        }
        totalPassengers += passengers.size();
    }

    /**
     * 入站日志
     */
    public void enterSiteRunLog(boolean checkEnd) {
        String action;
        if (checkEnd) {
            action = "抵达终点站";
            totalDriveTime += (time.intValue() - outSiteTime);
        } else {
            action = String.format("到达 %02d 站", this.siteCode);
        }
        busRunLogList.add(new BusRunLog(this.time, action));
    }

    /**
     * 出站日志
     */
    public void outSiteRunLog(boolean checkStart, Integer offNumber, Integer upNumber) {
        String action;
        if (checkStart) {
            action = String.format("从 %02d 站发车，乘客 %d 人", this.siteCode, upNumber);
            this.outSiteTime = time.intValue();
        } else {
            action = String.format("下客 %d 人，上客 %d 人，继续出发", upNumber, offNumber);
        }
        busRunLogList.add(new BusRunLog(this.time, action));
    }

    /**
     * 故障
     */
    public void faultRunLog(Integer pNumber) {
        String action = String.format("在 %02d 站故障，下客 %d 人", this.siteCode, pNumber);
        busRunLogList.add(new BusRunLog(this.time, action));
    }

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
