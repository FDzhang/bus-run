package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusConstant;
import lombok.Data;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private volatile LinkedList<Passenger> passengers;

    // --------------------------------------

    /**
     * 插队
     */
    public Passenger addPassenger(Passenger p) {
        lock.lock();
        try {
            passengers.addFirst(p);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return p;
    }

    /**
     * 公交到站、通知乘客上车
     * - 乘客上车
     */
    public List<Passenger> notifyPassengerUp(Bus bus) {
        List<Passenger> upPassengers = new ArrayList<>();
        lock.lock();
        try {
            Set<String> pSet = new HashSet<>();
            for (Passenger p : passengers) {
                if (p.getRouteCode().equals(bus.getRouteCode())) {
                    // 上车
                    p.upBus(bus);
                    pSet.add(p.getPassengerCode());
                    upPassengers.add(p);
                }
                if (bus.getPassengerMap().size() >= BusConstant.CAPACITY) {
                    break;
                }
            }
            passengers.removeIf(next -> pSet.contains(next.getPassengerCode()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return upPassengers;
    }

    @Override
    public String toString() {
        // 站点(code)   人数...
        return String.format("%02d\t %10s", this.siteCode, this.passengers.size());
    }


}
