package com.fine.friendlycc.bean;

/**
 * @author wulei
 */
public class FaceVerifyResultBean {

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