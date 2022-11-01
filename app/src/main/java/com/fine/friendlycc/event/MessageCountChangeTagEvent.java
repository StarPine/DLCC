package com.fine.friendlycc.event;

/**
 * Author: 彭石林
 * Time: 2021/12/27 15:53
 * Description: 讯息--聊天顶部角标展示
 */
public class MessageCountChangeTagEvent {
    private Integer textCount;

    public MessageCountChangeTagEvent(Integer textCount) {
        this.textCount = textCount;
    }

    public Integer getTextCount() {
        return textCount;
    }

    public void setTextCount(Integer textCount) {
        this.textCount = textCount;
    }
}
