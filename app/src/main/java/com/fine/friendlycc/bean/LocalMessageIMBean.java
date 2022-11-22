package com.fine.friendlycc.bean;

/**
 * Author: 彭石林
 * Time: 2021/10/25 10:08
 * Description: This is LocalMessageIMBean
 */
public class LocalMessageIMBean {

    private String msgId;
    private long sendTime;

    public LocalMessageIMBean(String msgId, long sendTime) {
        this.msgId = msgId;
        this.sendTime = sendTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}