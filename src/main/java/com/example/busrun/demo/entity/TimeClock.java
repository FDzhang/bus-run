package com.example.busrun.demo.entity;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : zxq
 * @create : 2022/3/17 19:15
 */
public class TimeClock {
    private final ReentrantLock lock = new ReentrantLock();
    final Condition r  = lock.newCondition();
    final Condition w  = lock.newCondition();

    private volatile AtomicLong time = new AtomicLong(0L);

    public AtomicLong getTime() {
        return time;
    }


}
