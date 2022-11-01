package com.fine.friendlycc.event;

/**
 * 真人認證
 *
 * @author wulei
 */
public class BadioEvent {
    private Integer type;//0:动态 1：节目

    public BadioEvent(Integer type) {
        this.type = type;
    }

    public BadioEvent() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
