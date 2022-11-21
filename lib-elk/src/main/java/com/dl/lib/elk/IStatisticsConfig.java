package com.dl.lib.elk;

public class IStatisticsConfig {
    private String serverUrl = "http://8.219.59.215:7400";
    //单条上报
    private boolean useDefaultLogServerUrl;
    //单条上限
    private int lgsCollectCount = 5;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public boolean isUseDefaultLogServerUrl() {
        return useDefaultLogServerUrl;
    }

    public void setUseDefaultLogServerUrl(boolean useDefaultLogServerUrl) {
        this.useDefaultLogServerUrl = useDefaultLogServerUrl;
    }

    public int getLgsCollectCount() {
        return lgsCollectCount;
    }

    public void setLgsCollectCount(int lgsCollectCount) {
        this.lgsCollectCount = lgsCollectCount;
    }
}
