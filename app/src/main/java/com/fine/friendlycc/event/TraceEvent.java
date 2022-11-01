package com.fine.friendlycc.event;

/**
 * Author: 彭石林
 * Time: 2021/8/3 18:25
 * Description: This is TraceEvent
 */
public class TraceEvent {
    int size;
    int selIdx;

    public TraceEvent(int size, int selIdx) {
        this.size = size;
        this.selIdx = selIdx;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSelIdx() {
        return selIdx;
    }

    public void setSelIdx(int selIdx) {
        this.selIdx = selIdx;
    }

    @Override
    public String toString() {
        return "TraceEvent{" +
                "size=" + size +
                ", selIdx=" + selIdx +
                '}';
    }
}
