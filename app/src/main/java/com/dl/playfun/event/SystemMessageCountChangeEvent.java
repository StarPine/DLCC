package com.dl.playfun.event;

/**
 * @author wulei
 */
public class SystemMessageCountChangeEvent {
    private int count;

    public SystemMessageCountChangeEvent() {
    }

    public SystemMessageCountChangeEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
