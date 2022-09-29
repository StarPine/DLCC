package com.dl.playfun.event;

/**
 * @ClassName CountDownTimerEvent
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/7/5 18:50
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class CountDownTimerEvent {
    private String text;

    public CountDownTimerEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}