package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusConstant;
import lombok.Data;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 公交车站点
 *
 * @author : zxq
 * @create : 2022/3/17 19:43
 */
@Data
public class BusSite {
    public BusSite() {
    }

    public BusSite(Integer code, TimeClock time) {
        this.siteCode = code;
        this.siteClock = time;
        this.passengers = new LinkedList<>();
    }

    Lock lock = new ReentrantLock();

    /**
     * 标准时钟
     */
    public TimeClock siteClock;

    /**
     * 站点编号
     */
    private Integer siteCode;

    // ------------------------

    /**
     * 站点的乘客
     * - <乘客编号，乘客>
     */
    private LinkedList<Passenger> passengers;

    // --------------------------------------

    /**
     * 公交到站、通知乘客上车
     * - 乘客上车
     */
    public List<Passenger> notifyPassengerUp(Bus bus) {
        Set<String> pSet = new HashSet<>();
        List<Passenger> upPassengers = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.getRouteCode().equals(bus.getRouteCode())) {
                // 上车
                p.upBus(bus);
                pSet.add(p.getPassengerCode());
                upPassengers.add(p);
            }
            if (bus.getPassengerMap().size() >= BusConstant.CAPACITY){
                break;
            }
        }
        passengers.removeIf(next -> pSet.contains(next.getPassengerCode()));
        return upPassengers;
    }

    @Override
    public String toString() {
        // 站点(code)   人数...
        return String.format("%02d\t %10s", this.siteCode, this.passengers.size());
    }


}
