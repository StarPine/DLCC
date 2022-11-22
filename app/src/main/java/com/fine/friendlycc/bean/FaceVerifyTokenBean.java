package com.fine.friendlycc.bean;

/**
 * @author wulei
 */
public class FaceVerifyTokenBean {

    private String RequestId;
    private String VerifyToken;
    private String BizId;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }

    public String getVerifyToken() {
        return VerifyToken;
    }

    public void setVerifyToken(String VerifyToken) {
        this.VerifyToken = VerifyToken;
    }

    public String getBizId() {
        return BizId;
    }

    public void setBizId(String BizId) {
        this.BizId = BizId;
    }
}