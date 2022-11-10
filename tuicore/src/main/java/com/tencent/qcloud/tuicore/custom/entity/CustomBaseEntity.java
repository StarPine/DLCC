package com.tencent.qcloud.tuicore.custom.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/8 16:29
 */
public class CustomBaseEntity<T> implements Serializable {
    /**
     * businessID : dl_custom_tmp
     * contentBody : {}
     */

    private String businessID;
    private ModuleMsgEntity<T> contentBody;

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public ModuleMsgEntity<T> getContentBody() {
        return contentBody;
    }

    public void setContentBody(ModuleMsgEntity<T> contentBody) {
        this.contentBody = contentBody;
    }
}
