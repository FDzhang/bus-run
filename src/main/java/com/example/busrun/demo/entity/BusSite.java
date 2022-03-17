package com.example.busrun.demo.entity;

import com.example.busrun.demo.constant.BusSiteTypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

    public BusSite(Integer code) {
        this.code = code;
        passengers = new LinkedList<>();
    }

    Lock lock = new ReentrantLock();

    /**
     * 站点编号
     */
    private Integer code;

    /**
     * 站点类型
     */
    private BusSiteTypeEnum siteType = BusSiteTypeEnum.NORMAL;

    /**
     * 站点当前的乘客
     */
    private LinkedList<Passenger> passengers;


    /**
     * 添加乘客
     */
    public void addPassenger(Passenger passenger) {
        lock.lock();
        try {
            this.passengers.add(passenger);
        } finally {
            lock.unlock();
        }
    }
    /**
     * 添加多个乘客
     */
    public void addPassengers(List<Passenger> passengerList) {
        lock.lock();
        try {
            this.passengers.addAll(passengerList);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 减少多个乘客
     */
    public List<Passenger> reducePassengers(int remain) {
        lock.lock();
        try {
            List<Passenger> res = new ArrayList<>();
            while (passengers.size() > 0 && remain > 0) {
                res.add(passengers.removeLast());
                remain--;
            }
            return res;
        } finally {
            lock.unlock();
        }
    }
}
