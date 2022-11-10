package com.tencent.qcloud.tuicore.custom.entity;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/12 10:43
 */
public class CallingRejectEntity implements Serializable {
    private String content;
    private int callingType;
    private int fromUserId;
    private int toUserId;
    private String inviterImId;
    private String receiverImId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCallingType() {
        return callingType;
    }

    public void setCallingType(int callingType) {
        this.callingType = callingType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getInviterImId() {
        return inviterImId;
    }

    public void setInviterImId(String inviterImId) {
        this.inviterImId = inviterImId;
    }

    public String getReceiverImId() {
        return receiverImId;
    }

    public void setReceiverImId(String receiverImId) {
        this.receiverImId = receiverImId;
    }
}
