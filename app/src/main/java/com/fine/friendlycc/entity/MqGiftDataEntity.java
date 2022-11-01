package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/9 14:53
 * 修改备注：公屏礼物bean
 */
public class MqGiftDataEntity implements Serializable {

    /**
     * messageType : sendGift
     * content : {
     * "fromUser":{"id":100877,"imId":"user_100877","sex":0,"nickname":"繼續","avatar":"images/avatare36c46245e2347d79677d0c78d776170.jpeg","isVip":1,"certification":1},
     * "toUser":{"id":161561,"imId":"user_161561","sex":1,"nickname":"小聰明 ","avatar":"images/avatar/bb632cfdf6b3466eab9e935cd5f81c90.jpg","isVip":0,"certification":1},
     * "giftName":"666","imagePath":"images/avatar/a682b05b59544b01bd6d1afb0c14a140.jpg","svgaPath":"","amount":1}
     */

    @SerializedName("messageType")
    private String messageType;
    @SerializedName("content")
    private MqBroadcastGiftEntity content;

    public String getMessageType() {
        return messageType;
    }

    public MqBroadcastGiftEntity getContent() {
        return content;
    }
}
