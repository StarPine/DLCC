package com.fine.friendlycc.entity;

/**
 * @author wulei
 */
public class FaceVerifyResultEntity {

    private int VerifyStatus;
    private String RequestId;

    public int getVerifyStatus() {
        return VerifyStatus;
    }

    public void setVerifyStatus(int VerifyStatus) {
        this.VerifyStatus = VerifyStatus;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }
}
