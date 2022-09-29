package com.dl.playfun.event;

/**
 * Author: 彭石林
 * Time: 2021/12/23 11:54
 * Description: This is BubbleTopShowEvent
 */
public class BubbleTopShowEvent {
    private boolean flag = false;

    public BubbleTopShowEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
