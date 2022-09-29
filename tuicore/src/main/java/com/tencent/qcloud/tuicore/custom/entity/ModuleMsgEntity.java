package com.tencent.qcloud.tuicore.custom.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/8 18:02
 */
public class ModuleMsgEntity<T> implements Serializable {
    /**
     * msgModuleName : pushMessage
     * contentBody : {}
     */

    private String msgModuleName;
    private CustomMsgTypeEntity<T> contentBody;

    public String getMsgModuleName() {
        return msgModuleName;
    }

    public void setMsgModuleName(String msgModuleName) {
        this.msgModuleName = msgModuleName;
    }

    public CustomMsgTypeEntity<T> getContentBody() {
        return contentBody;
    }

    public void setContentBody(CustomMsgTypeEntity<T> contentBody) {
        this.contentBody = contentBody;
    }
}
