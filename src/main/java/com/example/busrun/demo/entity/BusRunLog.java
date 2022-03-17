package com.example.busrun.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 公交车的运行明细
 * @author : zxq
 * @create : 2022/3/17 19:49
 */
@Data
public class BusRunLog {

    /**
     * 时间
     */
    private Long time;
    /**
     * 动作
     */
    private String action;

    public BusRunLog(Long time, String action) {
        this.time = time;
        this.action = action;
    }
}
