package com.fine.friendlycc.entity;

import java.io.Serializable;

/**
 * 描述：
 *
 * @Name： Friendly_CC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/11/16 15:13
 */
public class PaymentRelationBean implements Serializable {
    private int payerUserId;
    private int payeeUserId;
    private String payerImId;
    private String payeeImId;

    public int getPayerUserId() {
        return payerUserId;
    }

    public void setPayerUserId(int payerUserId) {
        this.payerUserId = payerUserId;
    }

    public int getPayeeUserId() {
        return payeeUserId;
    }

    public void setPayeeUserId(int payeeUserId) {
        this.payeeUserId = payeeUserId;
    }

    public String getPayerImId() {
        return payerImId;
    }

    public void setPayerImId(String payerImId) {
        this.payerImId = payerImId;
    }

    public String getPayeeImId() {
        return payeeImId;
    }

    public void setPayeeImId(String payeeImId) {
        this.payeeImId = payeeImId;
    }
}
