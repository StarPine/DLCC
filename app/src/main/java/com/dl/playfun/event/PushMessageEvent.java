package com.dl.playfun.event;

public class PushMessageEvent {
    private String type;

    public PushMessageEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
