package com.fine.friendlycc.bean;

public class HopeBean {
    private String name;
    private boolean isChoose;

    public HopeBean(String name, boolean isChoose) {
        this.name = name;
        this.isChoose = isChoose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}