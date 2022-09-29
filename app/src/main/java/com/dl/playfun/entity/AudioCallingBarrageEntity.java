package com.dl.playfun.entity;


import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/19 15:56
 */
public class AudioCallingBarrageEntity implements Serializable {
    String itemText;
    String imgPath;
    boolean sendGiftBag;

    public AudioCallingBarrageEntity(String itemText, String imgPath, boolean sendGiftBag) {
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
