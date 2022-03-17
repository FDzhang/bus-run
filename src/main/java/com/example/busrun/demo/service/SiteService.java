package com.example.busrun.demo.service;

import com.example.busrun.demo.constant.BusSiteConstant;
import com.example.busrun.demo.entity.BusSite;
import com.example.busrun.demo.entity.Passenger;
import com.example.busrun.demo.entity.TimeClock;
import com.example.busrun.demo.utils.RandomUtil;

import java.util.List;

/**
 * 站点服务
 * 定时产生乘客
 *
 * @author : zxq
 * @create : 2022/3/17 21:55
 */
public class SiteService implements Runnable {
    private final static int P_NUMBER = 10;

    private Long time = 0L;
    private TimeClock clock;

    private List<BusSite> busSiteList;
    private int siteBound;
    private Long limit = 0L;

    public SiteService(TimeClock clock, List<BusSite> busSiteList, Long limit) {
        this.clock = clock;
        this.busSiteList = busSiteList;
        this.siteBound = busSiteList.size();
        this.limit = limit;
    }

    @Override
    public void run() {
        if (clock.getTime().get() == time) {
            for (int i = 0; i < P_NUMBER; i++) {
                Passenger passenger = new Passenger(RandomUtil.busSiteRandom(siteBound), RandomUtil.busSiteRandom(siteBound));
                int site = RandomUtil.busSiteRandom(siteBound) - 1;
                busSiteList.get(site).addPassenger(passenger);
            }
            time += BusSiteConstant.CYCLE;
        }
    }
}
