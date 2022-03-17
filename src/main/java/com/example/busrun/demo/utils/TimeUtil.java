package com.example.busrun.demo.utils;

/**
 * @author : zxq
 * @create : 2022/3/18 0:29
 */
public class TimeUtil {

    /**
     * long 格式化显示， 小时:分钟
     */
    public static String long2TimeStr(Long time) {
        time /= 60;
        Long m = time % 60;
        Long h = time / 60;
        return String.format("%02d:%02d", h, m);
    }

}
