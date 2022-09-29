package com.dl.playfun.event;

/**
 * Author: 彭石林
 * Time: 2022/8/16 14:22
 * Description: 常联系事件发送数量
 */
public class MessageCountChangeContactEvent {
    private Integer textContactCount;

    public Integer getTextContactCount() {
        return textContactCount;
    }

    public void setTextContactCount(Integer textContactCount) {
        this.textContactCount = textContactCount;
    }


    public MessageCountChangeContactEvent(Integer textContactCount) {
        this.textContactCount = textContactCount;
    }

}
