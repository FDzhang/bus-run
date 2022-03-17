package com.example.busrun.demo.utils;

import java.util.Random;

/**
 * 随机工具类
 *
 * @author : zxq
 * @create : 2022/3/17 19:11
 */
public class RandomUtils {

    /**
     * 行驶时间的随机误差（秒） ， 0 or 1 分钟
     */
    public static int driveTimeRandom() {
        return new Random().nextInt(2) * 60;
    }

    /**
     * 随机故障
     * 故障几率：十分之一
     */
    public static boolean busFaultRandom() {
        return new Random().nextInt(10) % 10 == 1;
    }

    /**
     * 随机站点
     */
    public static int busSiteRandom(int bound) {
        return new Random().nextInt(bound) + 1;
    }

}
