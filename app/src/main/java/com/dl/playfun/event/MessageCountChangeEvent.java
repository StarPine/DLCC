package com.dl.playfun.event;

/**
 * @author wulei
 */
public class MessageCountChangeEvent {
    private int count;

    public MessageCountChangeEvent() {
    }

    public MessageCountChangeEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
