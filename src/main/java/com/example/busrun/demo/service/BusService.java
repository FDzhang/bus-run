package com.example.busrun.demo.service;

import com.example.busrun.demo.entity.Bus;
import com.example.busrun.demo.entity.Passenger;
import com.example.busrun.demo.utils.IRandomUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 公交车 服务
 * 在路线上运行
 *
 * @author : zxq
 * @create : 2022/3/17 21:51
 */
public class BusService implements Runnable {

    public BusService(Bus bus, CountDownLatch countDownLatch) {
        this.bus = bus;
        this.countDownLatch = countDownLatch;
    }

    private CountDownLatch countDownLatch;

    private Bus bus;

    @Override
    public void run() {
        busRun();
        countDownLatch.countDown();
    }

    /**
     * 处理某个公交某一时间点可能发生的事件
     * - 是否到站
     * - 公交入站
     * - 是否故障
     * -- 标记故障
     * -- 故障下车
     * - 通知乘客下车
     * - 通知站点到站
     * - 变车站信息
     * - 公交出站
     */
    public void busRun() {
        if (bus.getBusClock().getClock() == bus.getExpectedArriveTime()) {
            bus.enterBusSite();
            if (IRandomUtil.busFaultRandom()) {
                bus.busFault();
                bus.notifyPassengerOff();
            } else {
                List<Passenger> offPassenger = bus.notifyPassengerOff();
                List<Passenger> upPassenger = bus.notifyBusSiteArrive();

                bus.outBusSite(offPassenger.size(), upPassenger.size());
                bus.busToNext();
            }
        }
    }
}
