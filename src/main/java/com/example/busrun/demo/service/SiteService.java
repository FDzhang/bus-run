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

    private TimeClock clock;
    private TimeClock siteClock;
    private List<BusSite> busSiteList;
    private int siteBound;

    public SiteService(TimeClock clock,TimeClock siteClock, List<BusSite> busSiteList) {
        this.clock = clock;
        this.siteClock = siteClock;
        this.busSiteList = busSiteList;
        this.siteBound = busSiteList.size();
    }

    @Override
    public void run() {
        if (clock.getTime().get() == siteClock.getTime().get()) {
            for (int i = 0; i < SiteService.P_NUMBER; i++) {
                int source = RandomUtil.busSiteRandom(siteBound);
                int target = RandomUtil.busSiteRandom(siteBound);
                if (source != target) {
                    Passenger passenger = new Passenger(source, target, source < target ? 0 : 1);
                    busSiteList.get(source - 1).addPassenger(passenger);
                }
            }
            siteClock.getTime().getAndAdd(BusSiteConstant.CYCLE);

            System.err.println("站点(code)\t路线:人数...");
            for (BusSite site : busSiteList) {
                System.err.println(site.toString());
            }
        }
    }
}
