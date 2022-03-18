package com.example.busrun.demo.entity;

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
        this.code = code;
        this.time = time;
        passengerMap = new HashMap<>();
    }

    Lock lock = new ReentrantLock();

    public TimeClock time;

    /**
     * 站点编号
     */
    private Integer code;

    /**
     * 路线：乘客
     */
    private Map<Integer, LinkedList<Passenger>> passengerMap;


    /**
     * 添加乘客
     * 一个站点：归属多条路线
     * 将乘客添加到对应路线的集合中
     */
    public void addPassenger(Passenger passenger) {
        addPassenger(passenger, false);
    }

    public void addPassenger(Passenger passenger, boolean priority) {
        lock.lock();
        try {
            LinkedList<Passenger> rPassenger = passengerMap.getOrDefault(passenger.getRouteCode(), new LinkedList<>());
            if (priority) {
                rPassenger.addFirst(passenger);
            } else {
                rPassenger.addLast(passenger);
            }
            passengerMap.put(passenger.getRouteCode(), rPassenger);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 添加多个乘客
     * 一个站点：归属多条路线
     * 将乘客添加到对应路线的集合中
     */
    public void addPassengers(List<Passenger> passengerList) {
        addPassengers(passengerList, false);
    }

    public void addPassengers(List<Passenger> passengerList, boolean priority) {
        lock.lock();
        try {
            for (Passenger passenger : passengerList) {
                LinkedList<Passenger> rPassenger = passengerMap.getOrDefault(passenger.getRouteCode(), new LinkedList<>());
                if (priority) {
                    rPassenger.addFirst(passenger);
                } else {
                    rPassenger.addLast(passenger);
                }
                passengerMap.put(passenger.getRouteCode(), rPassenger);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 减少多个乘客
     * 一个站点：归属多条路线
     * 减少对应路线的乘客
     */
    public List<Passenger> reducePassengers(int remain, int routeCode) {
        lock.lock();
        try {
            LinkedList<Passenger> rPassenger = passengerMap.getOrDefault(routeCode, new LinkedList<>());
            List<Passenger> res = new ArrayList<>();
            while (rPassenger.size() > 0 && remain > 0) {
                res.add(rPassenger.removeFirst());
                remain--;
            }
            return res;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        // 站点(code)   路线:人数...
        StringBuilder rp = new StringBuilder();

        for (Integer routeCode : passengerMap.keySet()) {
            rp.append(routeCode).append(":").append(passengerMap.get(routeCode).size()).append(" ");
        }

        return String.format("%02d\t %15s", this.code, rp.toString());
    }
}
