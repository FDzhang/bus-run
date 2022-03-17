package com.example.busrun.demo.entity;

import lombok.Data;

/**
 * @author : zxq
 * @create : 2022/3/17 17:30
 */
@Data
public class Passenger {
    /**
     * 所在站点
     */
    private Integer curSite;
    /**
     * 目标站点
     */
    private Integer targetSite;

    public Passenger(Integer curSite, Integer targetSite) {
        this.curSite = curSite;
        this.targetSite = targetSite;
    }
}
