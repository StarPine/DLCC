package com.fine.friendlycc.bean;


import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/19 15:56
 */
public class AudioCallingBarrageBean implements Serializable {
    String itemText;
    String imgPath;
    boolean sendGiftBag;

    public AudioCallingBarrageBean(String itemText, String imgPath, boolean sendGiftBag) {
        this.itemText = itemText;
        this.imgPath = imgPath;
        this.sendGiftBag = sendGiftBag;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isSendGiftBag() {
        return sendGiftBag;
    }

    public void setSendGiftBag(boolean sendGiftBag) {
        this.sendGiftBag = sendGiftBag;
    }
}