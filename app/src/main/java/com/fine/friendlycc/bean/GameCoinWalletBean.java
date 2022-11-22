package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;

/**
 * @author wulei
 */
public class GameCoinWalletBean extends BaseObservable {

    /**
     * {
     *     "code": 0,
     *     "status": "success",
     *     "message": "success",
     *     "data": {
     *         "totalCoins": 97,
     *         "totalProfit": 73.98,
     *         "currencyName": "fake_data",
     *         "totalGameCoins": 3
     *       },
     *     "elapsed": 0
     * }
     */
    //账户钻石余额
//    @SerializedName("total_coin")
    private int totalCoins;
//    @SerializedName("can_coin")
    private double totalProfit;
//    @SerializedName("realname")
    private String currencyName;
//    @SerializedName("account_number")
    private long totalAppCoins;

    @Bindable
    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
        notifyPropertyChanged(BR.totalCoins);
    }

    @Bindable
    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Bindable
    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Bindable
    public long getTotalAppCoins() {
        return totalAppCoins;
    }

    public void setTotalGameCoins(long totalGameCoins) {
        this.totalAppCoins = totalGameCoins;
    }
}