package com.fine.friendlycc.event;

/**
 * @author wulei
 */
public class ApplyMessagePhotoStatusChangeEvent {
    private int applyId;

    public ApplyMessagePhotoStatusChangeEvent(int applyId) {
        this.applyId = applyId;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }
}
