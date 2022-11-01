package com.fine.friendlycc.event;

import com.fine.friendlycc.entity.MessageTagEntity;

/**
 * @ClassName MessageTagEvent
 * @Description 订阅分发推荐用标签显示
 * @Author 彭石林
 * @Date 2021/7/5 15:19
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class MessageTagEvent {
    private MessageTagEntity messageTagEntity;
    private boolean isShow = false;

    public MessageTagEvent(MessageTagEntity messageTagEntity, boolean isShow) {
        this.messageTagEntity = messageTagEntity;
        this.isShow = isShow;
    }

    public MessageTagEntity getMessageTagEntity() {
        return messageTagEntity;
    }

    public void setMessageTagEntity(MessageTagEntity messageTagEntity) {
        this.messageTagEntity = messageTagEntity;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}