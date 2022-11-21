package com.dl.lib.elk.log;


import java.io.Serializable;

public class AppLogEntity implements Serializable {
    public long createTime;
    public String statisticsString;

    public AppLogEntity(String statisticsString) {
        createTime = System.currentTimeMillis();
        this.statisticsString = statisticsString;
    }
}