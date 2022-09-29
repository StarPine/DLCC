package com.tencent.qcloud.tuikit.tuichat.bean;

import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;

import java.io.Serializable;

/**
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/6/29 15:32
 * 修改备注：自定义图片消息类型
 */
public class CustomImageMessage implements Serializable {
    public int version = TUIChatConstants.JSON_VERSION_UNKNOWN;
    public String businessID = TUIChatConstants.BUSINESS_ID_CUSTOM_IMAGE;//自定义消息id必须定义
    int type = 2001;
    private String imgPath ; //图片地址
    private int imgWidth;
    private int imgHeight;


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }
}
