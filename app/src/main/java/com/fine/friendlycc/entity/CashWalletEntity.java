package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class CashWalletEntity {

    /**
     * total_account : 5.00
     * can_account : 0.00
     */
    @SerializedName("total_account")
    private Float totalAccount;
    @SerializedName("can_account")
    private Float canAccount;

    public Float getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(Float totalAccount) {
        this.totalAccount = totalAccount;
    }

    public Float getCanAccount() {
        return canAccount;
    }

    public void setCanAccount(Float canAccount) {
        this.canAccount = canAccount;
    }
}
