package com.tencent.qcloud.tuicore.custom.entity;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/8 18:03
 */
public class CustomMsgTypeEntity<T> implements Serializable {
    private String customMsgType;
    private T customMsgBody;

    public String getCustomMsgType() {
        return customMsgType;
    }

    public void setCustomMsgType(String customMsgType) {
        this.customMsgType = customMsgType;
    }

    public T getCustomMsgBody() {
        return customMsgBody;
    }

    public void setCustomMsgBody(T customMsgBody) {
        this.customMsgBody = customMsgBody;
    }
}
