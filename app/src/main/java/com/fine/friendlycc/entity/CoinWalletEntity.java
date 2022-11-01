package com.fine.friendlycc.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class CoinWalletEntity extends BaseObservable {

    /**
     * total_coin : 0
     * can_coin : 0
     */
    //账户钻石余额
    @SerializedName("total_coin")
    private int totalCoin;
    @SerializedName("can_coin")
    private int canCoin;
    @SerializedName("realname")
    private String realName;
    @SerializedName("account_number")
    private String accountNumber;

    //账户台币余额
    @SerializedName("total_profit")
    private float totalProfit;

    public float getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Bindable
    public int getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(int totalCoin) {
        this.totalCoin = totalCoin;
        notifyPropertyChanged(BR.totalCoin);
    }

    @Bindable
    public int getCanCoin() {
        return canCoin;
    }

    public void setCanCoin(int canCoin) {
        this.canCoin = canCoin;
        notifyPropertyChanged(BR.canCoin);
    }


    @Bindable
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
        notifyPropertyChanged(BR.realName);
    }

    @Bindable
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        notifyPropertyChanged(BR.accountNumber);
    }
}
