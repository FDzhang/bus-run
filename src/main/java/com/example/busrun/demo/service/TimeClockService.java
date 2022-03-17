package com.example.busrun.demo.service;

import com.example.busrun.demo.entity.TimeClock;

/**
 * 时间
 *
 * @author : zxq
 * @create : 2022/3/17 21:55
 */
public class TimeClockService implements Runnable {

    private TimeClock timeClock;
    private Long limit;

    public TimeClockService(TimeClock timeClock, Long limit) {
        this.timeClock = timeClock;
        this.limit = limit;
    }

    @Override
    public void run() {
        while (timeClock.getTime().get() < limit) {
            timeClock.getTime().getAndDecrement();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
