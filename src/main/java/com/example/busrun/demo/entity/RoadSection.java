package com.example.busrun.demo.entity;


import lombok.Data;

/**
 * 路段
 *  A->B 为上行
 *  B->A 为下行
 * @author : zxq
 * @create : 2022/3/17 19:06
 */
@Data
public class RoadSection {
    /**
     * A站点编号
     */
    private Integer curSite;
    /**
     * B站点编号
     */
    private Integer nextSite;

    /**
     * 预计行程时间
     */
    private Integer distance;


    public RoadSection() {
    }

    public RoadSection(Integer curSite, Integer nextSite, Integer distance) {
        this.curSite = curSite;
        this.nextSite = nextSite;
        this.distance = distance;
    }
}
