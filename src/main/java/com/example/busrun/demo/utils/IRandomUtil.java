package com.example.busrun.demo.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 随机工具类
 *
 * @author : zxq
 * @create : 2022/3/17 19:11
 */
public class IRandomUtil {

    /**
     * 行驶时间的随机误差（秒） ， 0 or 1 分钟
     */
    public static int driveTimeRandom() {
        return RandomUtil.randomInt(2) * 60;
    }

    /**
     * 随机故障
     * 故障几率：十分之一
     */
    public static boolean busFaultRandom() {
        return RandomUtil.randomInt(10) % 10 == 1;
    }

    /**
     * 随机数 [min, max)
     */
    public static int randomNumber(int min, int max) {
        return RandomUtil.randomInt(Math.min(min, max), Math.max(min, max));
    }

    /**
     * 随机获取起点和终点
     * 起点 < 终点
     */
    public static <T> List<T> randomEleList(List<T> source) {
        int cur = IRandomUtil.randomNumber(0, source.size() - 1);
        int target = IRandomUtil.randomNumber(cur + 1, source.size());
        List<T> result = new ArrayList<>();

        result.add(source.get(cur));
        result.add(source.get(target));

        return result;
    }

    /**
     * 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return IdUtil.fastSimpleUUID();
    }


}
