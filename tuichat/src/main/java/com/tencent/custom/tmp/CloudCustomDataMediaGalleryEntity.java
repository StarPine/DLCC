package com.tencent.custom.tmp;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: 彭石林
 * Time: 2022/9/17 11:45
 * Description: This is CloudCustomDataMediaGalleryEntity
 */
public class CloudCustomDataMediaGalleryEntity implements Serializable {
    //红包视频/照片收益(该字段可能不返回)
    private BigDecimal redPackageRenvenue;
    //解锁价格，(该字段可能不返回)
    private BigDecimal unlockPrice;
    //是否未解锁，flase解锁，true没解锁，(该字段可能不返回)
    private boolean isUnLocked;
    //是否已经阅读(该字段可能不返回)
    private boolean isRead;
    //聊天信息的key 唯一标识
    private String msgKey;

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public BigDecimal getRedPackageRenvenue() {
        return redPackageRenvenue;
    }

    public void setRedPackageRenvenue(BigDecimal redPackageRenvenue) {
        this.redPackageRenvenue = redPackageRenvenue;
    }

    public BigDecimal getUnlockPrice() {
        return unlockPrice;
    }

    public void setUnlockPrice(BigDecimal unlockPrice) {
        this.unlockPrice = unlockPrice;
    }

    public boolean isUnLocked() {
        return isUnLocked;
    }

    public void setUnLocked(boolean unLocked) {
        isUnLocked = unLocked;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @NonNull
    @Override
    public String toString() {
        return "CloudCustomDataMediaGalleryEntity{" +
                "redPackageRenvenue=" + redPackageRenvenue +
                ", unlockPrice=" + unlockPrice +
                ", isUnLocked=" + isUnLocked +
                ", isRead=" + isRead +
                '}';
    }
}
