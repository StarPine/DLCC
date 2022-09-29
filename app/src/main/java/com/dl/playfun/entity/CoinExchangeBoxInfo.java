package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;

import java.util.List;

public class CoinExchangeBoxInfo extends BaseObservable {
    /**
     *         "totalCoins": 59,
     *         "totalGameCoins": 65,
     *         "priceList": [
     *             {
     *               "id": 1,
     *               "coins": 1000,
     *               "gameCoins": 1000
     *             },
     */

    private Integer totalCoins;
    private Integer totalAppCoins;
    private List<CoinExchangePriceInfo> priceList;

    @Bindable
    public Integer getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Integer totalCoins) {
        this.totalCoins = totalCoins;
        notifyPropertyChanged(BR.totalCoins);
    }

    @Bindable
    public Integer getTotalAppCoins() {
        return totalAppCoins;
    }

    public void setTotalAppCoins(Integer totalAppCoins) {
        this.totalAppCoins = totalAppCoins;
        notifyPropertyChanged(BR.totalAppCoins);
    }

    @Bindable
    public List<CoinExchangePriceInfo> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<CoinExchangePriceInfo> priceList) {
        this.priceList = priceList;
        notifyPropertyChanged(BR.priceList);
    }
}
