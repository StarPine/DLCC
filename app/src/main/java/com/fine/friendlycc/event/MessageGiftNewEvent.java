package com.fine.friendlycc.event;

import com.tencent.custom.GiftEntity;

/**
 * Author: 彭石林
 * Time: 2022/3/12 14:48
 * Description: This is MessageGiftNewEvent
 */
public class MessageGiftNewEvent {
    private GiftEntity giftEntity;
    //IM消息Id 做防抖用
    private String msgId;
    //消息发送放ID
    private String fromUser;


    private String formUserId;

    public MessageGiftNewEvent(GiftEntity giftEntity, String msgId, String formUserId) {
        this.giftEntity = giftEntity;
        this.msgId = msgId;
        this.formUserId = formUserId;
    }

    public String getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(String formUserId) {
        this.formUserId = formUserId;
    }

    public GiftEntity getGiftEntity() {
        return giftEntity;
    }

    public void setGiftEntity(GiftEntity giftEntity) {
        this.giftEntity = giftEntity;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
}
